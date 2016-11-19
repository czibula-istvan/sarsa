package ro.sarsa.clustering;

public interface IDistance<T> {
	public double distance(T a, T b);

	public double getIninity();
}
