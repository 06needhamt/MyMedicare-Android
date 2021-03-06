package com.needham.thomas.medicare.root.Classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tom Needham on 02/04/2016.
 * This singleton class represents a list of records generated by the app
 */
public class RecordList implements Serializable, IAppConstants {
    private ArrayList<Record> records;
    private static RecordList Instance;
    /**
     * constructs a new instance of RecordList
     * @param records the list of Records to use
     */
    private RecordList(ArrayList<Record> records)
    {
        if (this.records == null && records == null)
        {
            this.setRecords(new ArrayList<Record>());
        }
        else if(records != null)
        {
            this.setRecords(records);
        }
        Instance = this;

    }

    /**
     * get the currently active instance of this class or creates one if it does not exists
     * @return the currently active instance of this class
     */
    public static RecordList GetInstance()
    {
        if(Instance == null)
        {
            Instance = new RecordList(null);
            return Instance;
        }
        else
        {
            return Instance;
        }
    }

    public ArrayList<Record> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<Record> records) {
        this.records = records;
    }

    public void addRecord(Record r) {
        records.add(r);
    }
}
