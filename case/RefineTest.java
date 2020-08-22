import fenum.qual.Fenum;

class RefineTest {
  final static @Fenum("A") int A1 = 0;
  final static @Fenum("A") int A2 = 1;

  final static @Fenum("B") int B1 = 2;

  @Fenum("B") int field;

  void foo() {
	  int x = A1;
	  if(x == field) {
		System.out.println("B1");
	  }
  }
}
