public class StaticTest {

	static A a;
	static int x;

	A foo() {
		A local;
		//int i;
		local = a;
		//i = x;

		return local;
	}
/*
	int bar() {
		int i;
		i = x;

		return x;
	}

	A m1() {
		return foo();
	}
	*/
}

class A {
	int field;
}
