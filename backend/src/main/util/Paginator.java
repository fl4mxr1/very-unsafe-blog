package main.util;

import java.util.Arrays;

import org.json.JSONObject;

// A class used to turn an array of data into pages to avoid sending too much data over the network.
public class Paginator {
    public Object[] data;

    public Paginator() {}
    public Paginator(Object[] array) {
        this.data = array;
    }

    public Page getPage(int pageSize, int pageNumber) throws PageOutOfBoundsException {
        int startIndex = pageNumber * (pageSize - 1);
        int endIndex = Math.clamp(startIndex + pageSize, 0, data.length - 1);

        if (startIndex > data.length) {
            throw new PageOutOfBoundsException("Page out of bounds!");
        }

        Object[] pageData = Arrays.copyOfRange(data, startIndex, endIndex);
        Page page = new Page(pageData, pageSize, pageNumber);
        return page;
    }

    public int getPageCount(int pageSize) {
        return (int) Math.ceil(
            (double) data.length / (double) pageSize
        );
    }

    public static class PageOutOfBoundsException extends RuntimeException {
        public PageOutOfBoundsException(String message) {
            super(message);
        }

        @Override
        public String getMessage() {
            return super.getMessage();
        }
    }

    public static class Page {
        public final Object[] data;
        public final int pageSize;
        public final int pageNumber;

        public Page(Object[] data, int pageSize, int pageNumber) {
            this.data = data;
            this.pageSize = pageSize;
            this.pageNumber = pageNumber;
        }

        public JSONObject toJson() {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pageSize", this.pageSize);
            jsonObject.put("pageNumber", this.pageNumber);
            jsonObject.put("data", this.data);

            return jsonObject;
        }
    }
}
