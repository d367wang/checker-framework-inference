import java.util.List;

public class ListTest {
	int field;
  List<Integer> l;

	void m1() {
    l.add(0);
	}

	void m2() {
    if(l.contains(0)) {
      return;
    }
	}

}

