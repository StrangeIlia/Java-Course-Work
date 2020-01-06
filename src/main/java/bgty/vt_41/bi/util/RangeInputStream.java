package bgty.vt_41.bi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RangeInputStream extends InputStream {
    RandomAccessFile file;
    long start, curr, end;

    public RangeInputStream(RandomAccessFile file, long start) throws IOException {
        if(start > file.length()) throw new IllegalArgumentException("start > file size");
        this.file = file;
        this.start = start;
        this.curr = start;
        this.end = file.length();
    }

    public RangeInputStream(RandomAccessFile file, long start, long end) throws IOException {
        if(start > end) throw new IllegalArgumentException("start > end");
        if(start > file.length()) throw new IllegalArgumentException("start > file size");
        if(end > file.length()) throw new IllegalArgumentException("end > file size");
        this.file = file;
        this.start = start;
        this.curr = start;
        this.end = end;
    }

    @Override
    public void reset(){
        curr = start;
    }

    public int available() throws IOException {
        Long maxInteger = (long) Integer.MAX_VALUE;
        return (int)Long.min(maxInteger, end - curr);
    }

    @Override
    public int read() throws IOException {
        if(curr >= end) return -1;
        else{
            file.seek(curr);
            ++curr;
            return file.read();
        }
    }
}
