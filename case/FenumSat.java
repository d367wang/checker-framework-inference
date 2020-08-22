import fenum.qual.Fenum;

class Fenum {
  
  final static @Fenum("A") int A1 = 0;
  final static @Fenum("A") int A2 = 0;



  int get(int x) {
    return x;
  }

  void set() {
  }

  public static void main(String[] args) {
	switch(x) {
	case A1:
		System.out.println("A1");
		break;
	case A2:
		System.out.println("A2");
		break;
	default:
		System.out.println("default");
	}

  }
 
}

