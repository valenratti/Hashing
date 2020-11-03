import java.util.NoSuchElementException;
import java.util.function.Function;

public class ClosedHashing<K,V> extends Hash<K,V> {

    protected boolean[] states = new boolean[initialLookupSize]; //true si esta ocupado, false si es borrado logico

    public ClosedHashing(Function mappingFn) {
        super(mappingFn);
    }

    @Override
    public void insert(K key, V value) {
        int index = hash(key);
        Node<K, V> node;
        int firstLogicDelete = -1;
        boolean found = false;

        while ((node = LookUp[index]) != null && !found) {
            //Si la key es la misma, updateo el nodo<k,v>
            if (states[index] == true) {
                if (node.key.equals(key)) {
                    LookUp[index] = new Node<K, V>(key, value);
                    found = true;
                }
            }
            else {
                if (firstLogicDelete == -1)
                    firstLogicDelete = index;
            }
            if (index == LookUp.length - 1)
                index = 0;
            else index++;
        }


        if (!found) {
            int newIndex = firstLogicDelete == -1 ? index : firstLogicDelete;
            LookUp[newIndex] = new Node<K, V>(key, value);
            usedKeys++;
            states[newIndex] = true;
            loadFactor = ((double) usedKeys / initialLookupSize);
            if (loadFactor > THERSHOLD)
                duplicateSpaceAndRehash();
        }

    }

    @Override
    public void delete(K key) {
        int index = hash(key);
        Node<K, V> node;
        boolean found = false;
        while ((node = LookUp[index]) != null && !found) {
            if (states[index] == true && node.key.equals(key)) {
                found = true;
                if (LookUp[index + 1] == null) {
                    LookUp[index] = null;
                } else
                    states[index] = false;
                index++;
            }
        }
        if (found) {
            usedKeys--;
            states[index] = false;
            loadFactor = ((double) usedKeys / initialLookupSize);
            if (loadFactor > THERSHOLD)
                duplicateSpaceAndRehash();
        }
        else throw new NoSuchElementException("No se ha econtrado el elemento que desea eliminar");
    }
}


