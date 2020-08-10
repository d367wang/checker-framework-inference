//import org.checkerframework.dataflow.qual.Pure;
//import org.checkerframework.dataflow.qual.Deterministic;


public class PureTest {
	int field;

	//@Pure
	void foo() {
		//field = 0;
	}
	
	//@Deterministic 
	int bar(int x, int y) {
		foo();
		return x+y;

	}

	void m1(A a) {
		a.z = 0;
	}

	void m2(A a) {
		A aa = new A();
		aa.z = 0;
	}

}

class A {
	int z;
}
