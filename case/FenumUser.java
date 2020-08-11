import fenum.qual.Fenum;

class FenumUser {

  //@Fenum("ABC")final static int state1 = 0;
  @Fenum("ABC") final static int state1 = 0;
/*
  void foo() {
	  state1 = 0;
  }

*/


  int foo(int x) {
    int state0 = x;
    return x;
  }

  void bar() {
      foo(state1);
  }
 
}

