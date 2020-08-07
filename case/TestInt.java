import dataflow.qual.DataFlow;

public class TestInt {

	@DataFlow(typeNames={"java.lang.Integer"})
	Object a = 1;

	@DataFlow(typeNames={"int"})
	int b = Integer.valueOf(1);

	int c = 1;

    @DataFlow(typeNameRoots={"java.lang.Integer"})
    Integer d = Integer.valueOf(1);

    Object e = a;
}
