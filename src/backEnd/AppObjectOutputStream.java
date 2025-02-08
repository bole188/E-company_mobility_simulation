package backEnd;
import java.io.*;

public class AppObjectOutputStream extends ObjectOutputStream {
		public AppObjectOutputStream(OutputStream os) throws IOException{
			super(os);
		}
		@Override
		protected void writeStreamHeader() throws IOException{
			reset();
		}
}
