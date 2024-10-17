package heapdb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import heapdb.query.AndCondition;
import heapdb.query.Condition;
import heapdb.query.EqCondition;
import heapdb.query.OrCondition;
import heapdb.query.SelectQuery;

public class QueryTest {
	private Table table;
	
	@BeforeEach
	void init() {
		Schema instSchema = new Schema();  
    instSchema.addKeyIntType("ID");
		instSchema.addVarCharType("name", 30);
		instSchema.addVarCharType("dept_name", 15);
		instSchema.addIntType("salary");
		Tuple[] tuples = new Tuple[] {
				new Tuple(instSchema, 22222, "Einstein",    "Physics", 95000),
				new Tuple(instSchema, 12121, "Wu",          "Finance", 90000),
				new Tuple(instSchema, 32343, "El Said" ,    "History", 60000),
				new Tuple(instSchema, 45565, "Katz",        "Comp. Sci.", 75000),
				new Tuple(instSchema, 98345, "Kim",         "Elec. Eng.", 80000),
				new Tuple(instSchema, 10101, "Srinivasan" , "Comp. Sci.", 65000),
				new Tuple(instSchema, 76766, "Crick" ,      "Biology", 72000),
		};
		table = new Table(instSchema);
		for (int i = 0; i < tuples.length; i++) {
			table.insert(tuples[i]);
		}

	}
	
	@Test
	void simpleStrQuery() {
		// test a simple string equality query
		Condition cond = new EqCondition("dept_name", "Comp. Sci.");
		SelectQuery q = new SelectQuery(cond);
		ITable result = q.eval(table);
		assertEquals(result.size(), 2);
	}
	
	@Test
	void simpleNumQuery() {
		// test a simple numeric equality query
		Condition cond = new EqCondition("salary", 72000);
		SelectQuery q = new SelectQuery(cond);
		ITable result = q.eval(table);
		assertEquals(result.size(), 1);
	}
	
	@Test
	void emptyQuery() {
		// test a query that should return an empty table
		Condition cond = new EqCondition("salary", 12000);
		SelectQuery q = new SelectQuery(cond);
		ITable result = q.eval(table);
		assertEquals(result.size(), 0);
	}
	
	@Test
	void andQuery() {
		Condition cond = new AndCondition(new EqCondition("salary", 72000), new EqCondition("name", "Crick")); 
		SelectQuery q = new SelectQuery(cond);
		ITable tbl = q.eval(table);
		assertEquals(tbl.size(), 1);
	}

	@Test
	void orQuery() {
		Condition cond = new OrCondition(new EqCondition("salary", 60000), new EqCondition("dept_name", "Biology")); 
		SelectQuery q = new SelectQuery(cond);
		ITable tbl = q.eval(table);
		assertEquals(tbl.size(), 2);
	}
	
	@Test
	void badAttrQuery() {
		Condition cond = new EqCondition("salarie", 72000);
		SelectQuery q = new SelectQuery(cond);
		assertThrows(IllegalArgumentException.class, () -> q.eval(table));
	}
	
	@Test
	void badAttrOrQuery() {
		Condition cond = new OrCondition(new EqCondition("salarie", 60000), new EqCondition("dept_name", "Biology")); 
		SelectQuery q = new SelectQuery(cond);
		Assertions.assertThrows(IllegalArgumentException.class, () -> q.eval(table));
	}
	
	@Test
	void projectStrQuery() {
		// test a string equality query with projection
		Condition cond = new EqCondition("dept_name", "Comp. Sci.");
		SelectQuery q = new SelectQuery( new String[] {"name", "dept_name"}, cond);
		ITable result = q.eval(table);
		assertTrue(result.size() == 2 && result.getSchema().size() == 2);
	}

}
