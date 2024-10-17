package heapdb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import heapdb.query.SelectQuery;

class JoinTest {
	
	private Table instTable, deptTable;
	private Schema instSchema, deptSchema;

	@BeforeEach
	void init() {
		instSchema = new Schema();  // primary key ID int 
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
		instTable = new Table(instSchema);
		for (int i = 0; i < tuples.length; i++) {
			instTable.insert(tuples[i]);
		}
		
		// department table
		deptSchema = new Schema();   
		deptSchema.addKeyVarCharType("dept_name", 15);
		deptSchema.addVarCharType("building", 12);
		deptSchema.addIntType("budget");
		
		deptTable = new Table(deptSchema);	
		Tuple[] deptTuples = new Tuple[] {
				new Tuple(deptSchema, "Biology",     "Watson",  90000),
				new Tuple(deptSchema, "Comp. Sci.",  "Taylor", 100000),
				new Tuple(deptSchema, "Elec. Eng.",  "Taylor",  85000),
				new Tuple(deptSchema, "Finance",     "Painter",120000),
				new Tuple(deptSchema, "Music",       "Packard", 80000),
				new Tuple(deptSchema, "History",     "Painter", 50000),
				new Tuple(deptSchema, "Physics",     "Watson",  70000),
		};

		for (int i = 0; i < deptTuples.length; i++) {
			deptTable.insert(deptTuples[i]);
		}
	}

	
	@Test
	void singleIntPrimaryKey() {
		
		// try to insert tuple with same ID as Einstein
		Tuple dupTuple = new Tuple(instSchema, 22222, "Royce", "Physics", 85000);
		int n = instTable.size();
		boolean result = instTable.insert(dupTuple);
		assertTrue((!result) && instTable.size() == n);
	}
	
	@Test
	void singleStringPrimaryKey() {
		
		// try to insert a duplicate Physics department.
		Tuple dupTuple = new Tuple(deptSchema, "Physics", "Champman", 120000 );
		int n = deptTable.size();
		boolean result = deptTable.insert(dupTuple);
		assertTrue(!result && deptTable.size() == n);
	}

	
	@Test
	void schemaJoin() {
		Schema joined = instSchema.naturaljoin(deptSchema);
		assertTrue(joined != null && joined.size() == 6);
	}
	
	@Test
	void tableJoin() {
		Table joined = SelectQuery.naturalJoin(instTable, deptTable);
		assertTrue(joined != null && joined.size() == instTable.size());
	}
}