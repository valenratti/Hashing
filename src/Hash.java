import java.util.function.Function;

public class Hash<K,V> {
    protected int initialLookupSize= 50;
    protected static final double THERSHOLD = 0.75;
    protected int usedKeys = 0;
    protected double loadFactor = (double) usedKeys/initialLookupSize;
    protected int colision = 0;

    protected Node<K,V>[] LookUp= new Node[initialLookupSize];

    protected Function<? super K, Integer> prehash;

    public Hash( Function<? super K, Integer> mappingFn)
    {
        prehash= mappingFn;
    }

    protected int hash(K key)
    {
        if (key == null)
            throw new RuntimeException("No key provided");

        return prehash.apply(key) % LookUp.length;
    }


    public V getValue(K key)
    {
        Node<K, V> entry = get(key);
        if (entry == null)
            return null;

        return entry.value;
    }

    protected Node<K,V> get(K key)
    {
        return LookUp[  hash( key) ];
    }


    public void insert(K key, V value)
    {
        if(LookUp[hash(key)] != null)
            colision++;
        else {
            LookUp[hash(key)] = new Node<K, V>(key, value);
            usedKeys++;
            loadFactor = ((double) usedKeys / initialLookupSize);
            if (loadFactor > THERSHOLD)
                duplicateSpaceAndRehash();
        }
    }


    protected void duplicateSpaceAndRehash() {
        Node<K, V>[] aux = new Node[initialLookupSize];
        for(int i=0; i<initialLookupSize; i++)
            aux[i] = LookUp[i];
        initialLookupSize *= 2;
        LookUp = new Node [initialLookupSize];
        usedKeys = 0;
        for(Node<K, V> n : aux)
            if(n!=null)
                insert(n.key, n.value);
    }


    public int getColisions(){
        return colision;
    }


    public void delete(K key)
    {
        LookUp[  hash( key) ] = null;
    }

    public void dump()
    {
        for(int rec= 0; rec < LookUp.length; rec++)
            if (LookUp[rec] == null)
                System.out.println(String.format("slot %d is empty", rec));
            else
                System.out.println(String.format("slot %d contains %s", rec, LookUp[rec]));
    }



    static class Node<K,V>
    {
        final K key;
        V value;

        Node(K theKey, V theValue)
        {
            key= theKey;
            value= theValue;
        }


        public String toString()
        {
            return String.format("key=%s, value=%s", key, value );
        }
    }
}
