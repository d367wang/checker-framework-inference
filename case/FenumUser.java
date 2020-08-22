import fenum.qual.Fenum;

class FenumUser {

  @Fenum("ABC") 
  final static int state1 = 0;

  int foo(int x) {
    int state0 = x;
    return x;
  }

  void bar(int x) {
      foo(state1);

      switch(x) {
	case state1:
		System.out.println("state1");
		break;
	default:
		System.out.println("default");

      }
  }

 
}

