package SourceCode.Domain;

import SourceCode.Domain.ADT.*;
import SourceCode.Domain.Expression.ValueExp;
import SourceCode.Domain.Statements.IStmt;
import SourceCode.Domain.Value.StringValue;
import SourceCode.Domain.Value.Value;
import com.example.a7.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class PrgState {
    private MyIDictionary<String, Value> symTbl;

    public MyIStack<MyIDictionary<String, Value>> getSymTblStack() {
        return symTblStack;
    }

    private MyIStack<MyIDictionary<String, Value>> symTblStack;
    private MyIStack<IStmt> execution;
    private MyIList<Value> out;
    private IStmt originalProgram;
    private IHeap<Integer,Value> heap;

    private Integer id_thread;
    private Integer LastThreadId=1;
    private IProcTable<String, Pair<List<String>,IStmt>> proctable;

    private MyIDictionary<StringValue, BufferedReader> fileTable;

    public PrgState(MyIStack<MyIDictionary<String, Value>> symTbl, MyIStack<IStmt> execution, MyIList<Value> out, IStmt originalProgram
                    , MyIDictionary<StringValue,BufferedReader> fileTable, IHeap<Integer,Value> heap, Integer id, IProcTable<String, Pair<List<String>,IStmt>> proctable) {
        this.symTblStack = symTbl;
        if(symTbl.isEmpty()) {
            this.symTbl = new MyDictionary<>(new HashMap<>());
            this.symTblStack.push(this.symTbl);
        }
        else {
            this.symTbl = symTblStack.pop();
            symTblStack.push(this.symTbl);
        }
        this.execution = execution;
        this.out = out;
        this.originalProgram = originalProgram.deepCopy();
        this.fileTable = fileTable;
        this.heap = heap;
        this.id_thread = id;
        this.proctable = proctable;
        execution.push(originalProgram);

    }

    public IHeap<Integer, Value> getHeap() {
        return heap;
    }

    public IProcTable<String, Pair<List<String>, IStmt>> getProctable() {
        return proctable;
    }

    public boolean isNotCompleted(){
        return !(execution.isEmpty());
    }

    public Integer getId_thread(){
        return id_thread;
    }

    public Integer getLastThreadId() {
        return LastThreadId;
    }
    public void setLastThreadId(Integer id){
        LastThreadId = id;
    }

    public PrgState oneStep() throws MyException, IOException {
        if(execution.isEmpty()) throw new MyException("prgstate stack is empty");
        IStmt crtStmt = execution.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Thread:\n"+ id_thread.toString()+"\nExeStack:\n"+execution.toString()+"SymTable:\n"+symTbl.toString()+"Out:\n"+out.toString()+"FileTable:\n"
                +fileTable.toStringFileTable()+"Heap:\n"+heap.toString()+"\n\n";
    }

    public MyIDictionary<String, Value> getSymTable() {
        MyIDictionary<String,Value> copy= symTblStack.pop();
        symTblStack.push(copy);
        return copy;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIStack<IStmt> getStk() {
        return execution;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public MyIList<IStmt> getStackAsList() {
        MyIList<IStmt> stack = new MyList<>(new ArrayList<>());
        Stack<IStmt> helper = new Stack<>();
        while (!execution.isEmpty()) {
            IStmt crtStmt = execution.pop();
            stack.add(crtStmt);
            helper.push(crtStmt);
        }
        while (!helper.isEmpty()) {
            IStmt crtStmt = helper.pop();
            execution.push(crtStmt);
        }
        return stack;
    }

        public MyIList<MyIDictionary<String,Value>> getStackSymAsList() {
            MyIList<MyIDictionary<String,Value>> stack = new MyList<>(new ArrayList<>());
            Stack<MyIDictionary<String,Value>> helper = new Stack<>();
            while(!symTblStack.isEmpty()) {
                MyIDictionary<String,Value> crtStmt = symTblStack.pop();
                stack.add(crtStmt);
                helper.push(crtStmt);
            }
            while(!helper.isEmpty())
            {
                MyIDictionary<String,Value> crtStmt = helper.pop();
                symTblStack.push(crtStmt);
            }

        return stack;
    }
}
