package sneer.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.widget.Toast;

import java.io.Closeable;
import java.util.Map;

import sneer.android.impl.Envelope;
import sneer.android.impl.IPCProtocol;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.BIND_IMPORTANT;
import static android.os.Message.obtain;
import static sneer.android.impl.Envelope.envelope;
import static sneer.android.impl.IPCProtocol.ENVELOPE;

public class PartnerSession implements Closeable {

	public static PartnerSession join(Context context, Intent intent, Listener listener) {
		return new PartnerSession(context, intent, listener);
	}

	public boolean wasStartedByMe() {
		throw new RuntimeException("Not implemented yet");
	}

	public void send(Object payload) {
		sendToSneer(payload);
	}

	public interface Listener {
		void onUpToDate();
		void onMessage(Message message);
    }


	@Override
	public void close() {
		context.unbindService(connection);
	}


	private final Context context;
    private final Listener listener;
	private final ServiceConnection connection = createConnection();
	private Messenger toSneer;


	private ServiceConnection createConnection() {
		return new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				toSneer = new Messenger(service);
				Messenger callback = new Messenger(new FromSneerHandler());
				sendToSneer(callback);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				toSneer = null;
			}
		};
	}


    private PartnerSession(Context context, Intent intent, Listener listener) {
		this.context = context;
        this.listener = listener;
		context.bindService(
			intent.<Intent>getParcelableExtra(IPCProtocol.JOIN_SESSION),
			connection,
			BIND_AUTO_CREATE | BIND_IMPORTANT
		);
    }


	private void sendToSneer(Object data) {
		android.os.Message msg = obtain();
		Bundle bundle = new Bundle();
		bundle.putParcelable(ENVELOPE, envelope(data));
		msg.setData(bundle);
		try {
			doSendToSneer(msg);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void doSendToSneer(android.os.Message msg) throws Exception {
		if (toSneer == null) throw new Exception("Connection to Sneer was lost.");
		toSneer.send(msg);
	}


	private class FromSneerHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			data.setClassLoader(Envelope.class.getClassLoader());
			Object content = ((Envelope) data.getParcelable(ENVELOPE)).content;
			if (content.equals("upToDate"))
				listener.onUpToDate();
			else
				listener.onMessage(getMessage(content));

		}

	}

	private Message getMessage(Object content) {
		final Map<String, Object> map = (Map<String, Object>)content;
		return new Message() {
			@Override
			public boolean wasSentByMe() {
				return (boolean) map.get("wasSentByMe");
			}

			@Override
			public Object payload() {
				return map.get("payload");
			}
		};
	}


	private void handleException(Exception e) {
		e.printStackTrace();
		Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	}

}
