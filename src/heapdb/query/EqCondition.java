package heapdb.query;

import heapdb.Tuple;

/**
 * A condition of the form colName = value.
 * 
 * @author Glenn
 *
 */

public class EqCondition extends Condition {
	
	private String colName;
	private Object value;
	
	public EqCondition(String colName, Object value) {
		this.colName = colName;
		this.value = value;
	}
	
	public String getColumnName() {
		return colName;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public Boolean eval(Tuple tuple) {
		// TODO compare the value of the tuple column to the value variable.
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return colName+" = "+value;
	}
}
