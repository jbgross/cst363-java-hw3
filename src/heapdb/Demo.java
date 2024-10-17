package heapdb;

public class Demo {

	public static void main(String[] args) {
		Schema schema = new Schema();
		schema.addKeyIntType("ID");
		schema.addVarCharType("name", 25);
		schema.addVarCharType("dept_name", 25);
		schema.addIntType("salary");
		
		Table table = new Table(schema);
		table.insert(new Tuple(schema, 12121, "Kim", "Elect. Engr.", 65000));
		table.insert(new Tuple(schema, 19803, "Wisneski", "Comp. Sci.", 46000));
		table.insert(new Tuple(schema, 24734, "Gross", "Comp. Sci.", 70000));
		table.insert(new Tuple(schema, 55552, "Scott", "Math", 80000));
		table.insert(new Tuple(schema,12321, "Tao", "Comp. Sci.", 95000));
		System.out.println(table.toString());
		System.out.println();
		
		boolean b = table.insert(new Tuple(schema, 19803, "Dupl", "Comp. Sci", 80000));
		System.out.println("Attempt to insert duplibcate 19803. Should be false. Actual result "+b);
		System.out.println();
		
		System.out.println("delete 12121. Should be true. Actual result "+ table.delete(12121));
		System.out.println();
		System.out.println(table.toString());
		System.out.println();

		System.out.println("delete 12121. Should be false. Actual result " + table.delete(12121));
		System.out.println();
		
		System.out.println("lookup 19803 "+ table.lookup(19803));
		System.out.println();
		
		System.out.println("lookup 12121 " + table.lookup(12121));
		System.out.println();
		
		System.out.println("eval dept_name='Comp. Sci'");
		System.out.println(table.lookup("dept_name", "Comp. Sci."));
		System.out.println();
		
		System.out.println("eval ID=19803");
		System.out.println(table.lookup("ID",  19803));
		System.out.println();
		
		System.out.println("eval ID=19802");
		System.out.println(table.lookup("ID",  19802));
		System.out.println();
		
	}

}
