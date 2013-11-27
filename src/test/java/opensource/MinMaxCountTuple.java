package opensource;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class MinMaxCountTuple implements Writable{

	public void readFields(DataInput in) throws IOException {
		
	}

	public void write(DataOutput out) throws IOException {
		out.writeLong(1);
		
	}

}
