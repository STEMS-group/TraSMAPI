package trasmapi.sumo;

public class Pair<T1, T2> {
	T1 first;
	T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	public T1 first() {
		return first;
	}
	
	public T2 second() {
		return second;
	}
}
