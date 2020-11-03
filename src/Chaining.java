import java.util.*;
import java.util.function.Function;

public class Chaining<K, V> extends Hash<K, V> {
    private TreeSet<ComparableNode<K,V>>[] LookUpList = new TreeSet[initialLookupSize];   //array de TreeSets

    public Chaining(Function<? super K, Integer> mappingFn) {
        super(mappingFn);
    }


    @Override
    public void insert(K key, V value) {
        int index = hash(key);
        int sizeBefore, sizeAfter;
        if(LookUpList[index] == null) {
            sizeBefore = 0;
            LookUpList[index] = new TreeSet<>();
        }
        else
            sizeBefore = LookUpList[index].size();
        LookUpList[index].add(new ComparableNode<>(key, value, prehash.apply(key)));
        sizeAfter = LookUpList[index].size();
        if(sizeAfter!=sizeBefore)
            usedKeys++;
        loadFactor = ((double) usedKeys / initialLookupSize);
            if (loadFactor > THERSHOLD)
                duplicateSpaceAndRehash();
    }

    @Override
    protected void duplicateSpaceAndRehash() {
        TreeSet<ComparableNode<K,V>>[] aux = new TreeSet[initialLookupSize];
        System.arraycopy(LookUpList, 0, aux, 0, initialLookupSize);
        initialLookupSize *= 2;
        LookUpList = new TreeSet[initialLookupSize];
        usedKeys = 0;
        for(TreeSet<ComparableNode<K, V>> list : LookUpList)
            for (ComparableNode<K, V> n : list)
                    insert(n.key, n.value);
    }


    @Override
    public void delete(K key) {
        int index = hash(key);
        /*if (LookUpList[index] != null) {
            for (ComparableNode<K, V> n : LookUpList[index]) {
                int keyPrehash = prehash.apply(key);
                if (n.prehash > keyPrehash)
                    return;
                if (n.prehash == keyPrehash)
                    LookUpList[index].remove(n);
            }
            if (LookUpList[index].size() == 0) {
                usedKeys--;
                LookUpList[index] = null;
            }
        }*/
        ComparableNode<K, V> node = get(key);
        if(node!=null){
            LookUpList[index].remove(node);
            usedKeys--;
            if (LookUpList[index].size() == 0) {
                LookUpList[index] = null;
            }
        }
    }

    @Override
    public ComparableNode<K, V> get(K key){   //search
        int index = hash(key);
        if(LookUpList[index] != null)
            for(ComparableNode<K, V> n : LookUpList[index]){
                int keyPrehash = prehash.apply(key);
                if(n.prehash > keyPrehash)
                    return null;
                if(n.prehash == keyPrehash)
                    return n;
            }
        return null;
    }

    @Override
    public void dump() {
        for (int rec = 0; rec < LookUpList.length; rec++)
            if (LookUpList[rec] == null)
                System.out.println(String.format("slot %d is empty", rec));
            else{
                System.out.println(String.format("slot %d contains %s", rec, LookUpList[rec]));
            }

    }


    static class ComparableNode<K, V> extends Node<K, V>
            implements Comparable<ComparableNode<K, V>>{
        int prehash;
        ComparableNode(K theKey, V theValue, int thePrehash) {
            super(theKey, theValue);
            prehash = thePrehash;
        }

        @Override
        public int compareTo(ComparableNode<K, V> o) {
            return this.prehash - o.prehash;
        }
    }
}
