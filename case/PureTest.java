import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.Deterministic;


public class PureTest {
	int field;

	//@Pure
	void foo(int x, int y) {
		field = 0;
	}
	
	//@Deterministic 
	int bar(int x, int y) {
		foo();
		return x+y;

	}

}

