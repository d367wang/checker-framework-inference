import fenum.qual.Fenum;

class FenumUser {

  @Fenum("ABC")final static int state1 = 0;
  int state0;

  int foo(int x) {
    state0 = x;
    return x;
  }

  void bar() {
	  foo(state1);
  }
}






