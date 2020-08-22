import fenum.qual.Fenum;

class FenumUnsat {
  
  final static @Fenum("A") int A1 = 0;
  final static @Fenum("A") int A2 = 1;

  final static @Fenum("B") int B1 = 2;

  public void foo(int x) {
     switch(x) {
	case A1:
		System.out.println("A1");
		break;
	case A2:
		System.out.println("A2");
		break;
	case B1:
		System.out.println("B1");
		break;
	default:
		System.out.println("default");
     }
  }
}







