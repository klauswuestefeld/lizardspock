package sneer.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import java.io.Closeable;
import java.util.List;

public class PartnerSession implements Closeable {

	public static PartnerSession join(Context context, Intent intent, Listener listener) {
		return new PartnerSession(context, intent, listener);
	}

	public void send(Object payload) {
		if (messenger != null) {
			android.os.Message msg = android.os.Message.obtain();
			msg.obj = payload;
			try {
				messenger.send(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	public interface Listener {
		void onHistoryReplay(List<Message> history);
		void onNewMessage(Message message);
    }

    private final Listener listener;
	private Messenger messenger;
	private ServiceConnection conn;
	private Context context;

    private PartnerSession(final Context context, final Intent intent, final Listener listener) {
        this.listener = listener;
		this.conn = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				messenger = new Messenger(service);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				messenger = null;
			}
		};
		this.context = context;
		context.bindService(
				intent.<Intent>getParcelableExtra("JOIN_SESSION"),
				conn,
				Context.BIND_AUTO_CREATE
		);
    }

	@Override
	public void close() {
		context.unbindService(conn);
	}
}
