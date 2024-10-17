package heapdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table implements ITable {
	
	private List<Tuple> tuples;
	private Schema schema;
	
	public Table(Schema schema) {
		this.schema = schema;
		tuples = new ArrayList<>();
	}
	
	@Override
	public Schema getSchema() {
		return schema;
	}

	
	@Override
	public int size() {
		return tuples.size();
	}

	@Override
	public void close() {
		// do nothing
	}
	
	@Override
	public boolean insert(Tuple rec) {
		if (! rec.getSchema().equals(schema)) {
			throw new IllegalStateException("Error: tuple schema does not match table schema.");
		}
		
		// if schema has no key, then just add the tuple.
		if (schema.getKey()==null) {
			tuples.add(rec);
			return true;
		}
		for (Tuple t: this) {
			if (t.getKey().equals(rec.getKey())) {
				return false; //duplicate key
			}
		}
		// add a copy of the Tuple to do prevent modification of the tuple after insert.
		tuples.add(new Tuple(rec));
		return true;
	}

	@Override
	public boolean delete(Object key) {
		if (schema.getKey() == null) {
			throw new IllegalStateException("Error: table does not have a primary key.  Can not delete.");
		}
		for (int i=0; i< tuples.size(); i++) {
			if (tuples.get(i).getKey().equals(key)) {
				tuples.remove(i);
				return true;
			}
		}
		return false; // key not found.
	}
	

	@Override
	public Tuple lookup(Object key) {
		if (schema.getKey() == null) {
			throw new IllegalStateException("Error: table does not have a primary key.  Can not lookup by key.");
		}

		for (Tuple t: tuples) {
			if (t.getKey().equals(key)) return new Tuple(t); // return copy of tuple
		}
		return null;  // not found

	}

	@Override
	public ITable lookup(String columnName, Object value) {
		if (schema.getColumnIndex(columnName) < 0) {
			throw new IllegalStateException("Error: table does not contain column "+ columnName);
		}
		Table result = new Table(this.getSchema());
		
		// find all tuples that satisfy the predicate colname=value
		// and insert the tuples to result table.
		// return the result		
		for (Tuple t: tuples) {
			if (t.get(columnName).equals(value)) result.insert(t);
		}
		return result;
	}

	@Override
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}
	
	public String toString() {
		if (tuples.isEmpty()) {
			return "Empty Table";
		} else {
			StringBuilder sb = new StringBuilder();
			for (Tuple t : this) {
				sb.append(t.toString());
				sb.append("\n");
			}
			return sb.toString();
		}
	}
}
