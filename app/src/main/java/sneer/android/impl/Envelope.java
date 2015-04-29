package sneer.android.impl;

import android.os.Parcel;
import android.os.Parcelable;

public class Envelope implements Parcelable {

	final Object value;

	
	public static Envelope of(Object o) {
		return new Envelope(o);
	}

	
	Envelope(Object value) {
		this.value = value;
	}
	
	
	@Override
	public int describeContents() {
		return 0;
	}

	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(value);
	}

	
	public static final Creator<Envelope> CREATOR = new Creator<Envelope>() {
		public Envelope createFromParcel(Parcel in) {
			return Envelope.of(in.readValue(null));
		}
		
		public Envelope[] newArray(int size) {
			return new Envelope[size];
		}
	};

	
	public Object get() {
		return value;
	}
	
	
	@Override
	public String toString() {
		return "Value(" + value + ")";
	}

	
	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Envelope other = (Envelope) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
