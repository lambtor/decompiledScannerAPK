package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

abstract class MapCollections {
   MapCollections.EntrySet mEntrySet;
   MapCollections.KeySet mKeySet;
   MapCollections.ValuesCollection mValues;

   public static boolean containsAllHelper(Map var0, Collection var1) {
      Iterator var3 = var1.iterator();

      boolean var2;
      while(true) {
         if (var3.hasNext()) {
            if (var0.containsKey(var3.next())) {
               continue;
            }

            var2 = false;
            break;
         }

         var2 = true;
         break;
      }

      return var2;
   }

   public static boolean equalsSetHelper(Set var0, Object var1) {
      boolean var4 = true;
      boolean var3 = false;
      boolean var2;
      if (var0 == var1) {
         var2 = true;
      } else {
         var2 = var3;
         if (var1 instanceof Set) {
            Set var7 = (Set)var1;

            label26: {
               try {
                  if (var0.size() != var7.size()) {
                     break label26;
                  }

                  var2 = var0.containsAll(var7);
               } catch (NullPointerException var5) {
                  var2 = var3;
                  return var2;
               } catch (ClassCastException var6) {
                  var2 = var3;
                  return var2;
               }

               if (var2) {
                  var2 = var4;
                  return var2;
               }
            }

            var2 = false;
         }
      }

      return var2;
   }

   public static boolean removeAllHelper(Map var0, Collection var1) {
      int var2 = var0.size();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         var0.remove(var4.next());
      }

      boolean var3;
      if (var2 != var0.size()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public static boolean retainAllHelper(Map var0, Collection var1) {
      int var2 = var0.size();
      Iterator var4 = var0.keySet().iterator();

      while(var4.hasNext()) {
         if (!var1.contains(var4.next())) {
            var4.remove();
         }
      }

      boolean var3;
      if (var2 != var0.size()) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   protected abstract void colClear();

   protected abstract Object colGetEntry(int var1, int var2);

   protected abstract Map colGetMap();

   protected abstract int colGetSize();

   protected abstract int colIndexOfKey(Object var1);

   protected abstract int colIndexOfValue(Object var1);

   protected abstract void colPut(Object var1, Object var2);

   protected abstract void colRemoveAt(int var1);

   protected abstract Object colSetValue(int var1, Object var2);

   public Set getEntrySet() {
      if (this.mEntrySet == null) {
         this.mEntrySet = new MapCollections.EntrySet();
      }

      return this.mEntrySet;
   }

   public Set getKeySet() {
      if (this.mKeySet == null) {
         this.mKeySet = new MapCollections.KeySet();
      }

      return this.mKeySet;
   }

   public Collection getValues() {
      if (this.mValues == null) {
         this.mValues = new MapCollections.ValuesCollection();
      }

      return this.mValues;
   }

   public Object[] toArrayHelper(int var1) {
      int var3 = this.colGetSize();
      Object[] var4 = new Object[var3];

      for(int var2 = 0; var2 < var3; ++var2) {
         var4[var2] = this.colGetEntry(var2, var1);
      }

      return var4;
   }

   public Object[] toArrayHelper(Object[] var1, int var2) {
      int var4 = this.colGetSize();
      Object[] var5 = var1;
      if (var1.length < var4) {
         var5 = (Object[])Array.newInstance(var1.getClass().getComponentType(), var4);
      }

      for(int var3 = 0; var3 < var4; ++var3) {
         var5[var3] = this.colGetEntry(var3, var2);
      }

      if (var5.length > var4) {
         var5[var4] = null;
      }

      return var5;
   }

   final class ArrayIterator implements Iterator {
      boolean mCanRemove = false;
      int mIndex;
      final int mOffset;
      int mSize;

      ArrayIterator(int var2) {
         this.mOffset = var2;
         this.mSize = MapCollections.this.colGetSize();
      }

      public boolean hasNext() {
         boolean var1;
         if (this.mIndex < this.mSize) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Object next() {
         Object var1 = MapCollections.this.colGetEntry(this.mIndex, this.mOffset);
         ++this.mIndex;
         this.mCanRemove = true;
         return var1;
      }

      public void remove() {
         if (!this.mCanRemove) {
            throw new IllegalStateException();
         } else {
            --this.mIndex;
            --this.mSize;
            this.mCanRemove = false;
            MapCollections.this.colRemoveAt(this.mIndex);
         }
      }
   }

   final class EntrySet implements Set {
      public boolean add(Entry var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         int var2 = MapCollections.this.colGetSize();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            MapCollections.this.colPut(var5.getKey(), var5.getValue());
         }

         boolean var3;
         if (var2 != MapCollections.this.colGetSize()) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         boolean var3 = false;
         if (var1 instanceof Entry) {
            Entry var4 = (Entry)var1;
            int var2 = MapCollections.this.colIndexOfKey(var4.getKey());
            if (var2 >= 0) {
               var3 = ContainerHelpers.equal(MapCollections.this.colGetEntry(var2, 1), var4.getValue());
            }
         }

         return var3;
      }

      public boolean containsAll(Collection var1) {
         Iterator var3 = var1.iterator();

         boolean var2;
         while(true) {
            if (var3.hasNext()) {
               if (this.contains(var3.next())) {
                  continue;
               }

               var2 = false;
               break;
            }

            var2 = true;
            break;
         }

         return var2;
      }

      public boolean equals(Object var1) {
         return MapCollections.equalsSetHelper(this, var1);
      }

      public int hashCode() {
         int var1 = 0;

         for(int var2 = MapCollections.this.colGetSize() - 1; var2 >= 0; --var2) {
            Object var5 = MapCollections.this.colGetEntry(var2, 0);
            Object var6 = MapCollections.this.colGetEntry(var2, 1);
            int var3;
            if (var5 == null) {
               var3 = 0;
            } else {
               var3 = var5.hashCode();
            }

            int var4;
            if (var6 == null) {
               var4 = 0;
            } else {
               var4 = var6.hashCode();
            }

            var1 += var4 ^ var3;
         }

         return var1;
      }

      public boolean isEmpty() {
         boolean var1;
         if (MapCollections.this.colGetSize() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Iterator iterator() {
         return MapCollections.this.new MapIterator();
      }

      public boolean remove(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         throw new UnsupportedOperationException();
      }

      public Object[] toArray(Object[] var1) {
         throw new UnsupportedOperationException();
      }
   }

   final class KeySet implements Set {
      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         boolean var2;
         if (MapCollections.this.colIndexOfKey(var1) >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean containsAll(Collection var1) {
         return MapCollections.containsAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public boolean equals(Object var1) {
         return MapCollections.equalsSetHelper(this, var1);
      }

      public int hashCode() {
         int var2 = 0;

         for(int var1 = MapCollections.this.colGetSize() - 1; var1 >= 0; --var1) {
            Object var4 = MapCollections.this.colGetEntry(var1, 0);
            int var3;
            if (var4 == null) {
               var3 = 0;
            } else {
               var3 = var4.hashCode();
            }

            var2 += var3;
         }

         return var2;
      }

      public boolean isEmpty() {
         boolean var1;
         if (MapCollections.this.colGetSize() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Iterator iterator() {
         return MapCollections.this.new ArrayIterator(0);
      }

      public boolean remove(Object var1) {
         int var2 = MapCollections.this.colIndexOfKey(var1);
         boolean var3;
         if (var2 >= 0) {
            MapCollections.this.colRemoveAt(var2);
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public boolean removeAll(Collection var1) {
         return MapCollections.removeAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public boolean retainAll(Collection var1) {
         return MapCollections.retainAllHelper(MapCollections.this.colGetMap(), var1);
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(0);
      }

      public Object[] toArray(Object[] var1) {
         return MapCollections.this.toArrayHelper(var1, 0);
      }
   }

   final class MapIterator implements Iterator, Entry {
      int mEnd = MapCollections.this.colGetSize() - 1;
      boolean mEntryValid = false;
      int mIndex = -1;

      public final boolean equals(Object var1) {
         boolean var3 = true;
         boolean var2 = false;
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            if (var1 instanceof Entry) {
               Entry var4 = (Entry)var1;
               if (ContainerHelpers.equal(var4.getKey(), MapCollections.this.colGetEntry(this.mIndex, 0)) && ContainerHelpers.equal(var4.getValue(), MapCollections.this.colGetEntry(this.mIndex, 1))) {
                  var2 = var3;
               } else {
                  var2 = false;
               }
            }

            return var2;
         }
      }

      public Object getKey() {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return MapCollections.this.colGetEntry(this.mIndex, 0);
         }
      }

      public Object getValue() {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return MapCollections.this.colGetEntry(this.mIndex, 1);
         }
      }

      public boolean hasNext() {
         boolean var1;
         if (this.mIndex < this.mEnd) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public final int hashCode() {
         int var2 = 0;
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            Object var4 = MapCollections.this.colGetEntry(this.mIndex, 0);
            Object var3 = MapCollections.this.colGetEntry(this.mIndex, 1);
            int var1;
            if (var4 == null) {
               var1 = 0;
            } else {
               var1 = var4.hashCode();
            }

            if (var3 != null) {
               var2 = var3.hashCode();
            }

            return var2 ^ var1;
         }
      }

      public Entry next() {
         ++this.mIndex;
         this.mEntryValid = true;
         return this;
      }

      public void remove() {
         if (!this.mEntryValid) {
            throw new IllegalStateException();
         } else {
            MapCollections.this.colRemoveAt(this.mIndex);
            --this.mIndex;
            --this.mEnd;
            this.mEntryValid = false;
         }
      }

      public Object setValue(Object var1) {
         if (!this.mEntryValid) {
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
         } else {
            return MapCollections.this.colSetValue(this.mIndex, var1);
         }
      }

      public final String toString() {
         return this.getKey() + "=" + this.getValue();
      }
   }

   final class ValuesCollection implements Collection {
      public boolean add(Object var1) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection var1) {
         throw new UnsupportedOperationException();
      }

      public void clear() {
         MapCollections.this.colClear();
      }

      public boolean contains(Object var1) {
         boolean var2;
         if (MapCollections.this.colIndexOfValue(var1) >= 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public boolean containsAll(Collection var1) {
         Iterator var3 = var1.iterator();

         boolean var2;
         while(true) {
            if (var3.hasNext()) {
               if (this.contains(var3.next())) {
                  continue;
               }

               var2 = false;
               break;
            }

            var2 = true;
            break;
         }

         return var2;
      }

      public boolean isEmpty() {
         boolean var1;
         if (MapCollections.this.colGetSize() == 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         return var1;
      }

      public Iterator iterator() {
         return MapCollections.this.new ArrayIterator(1);
      }

      public boolean remove(Object var1) {
         int var2 = MapCollections.this.colIndexOfValue(var1);
         boolean var3;
         if (var2 >= 0) {
            MapCollections.this.colRemoveAt(var2);
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public boolean removeAll(Collection var1) {
         int var3 = MapCollections.this.colGetSize();
         boolean var6 = false;

         int var4;
         for(int var2 = 0; var2 < var3; var3 = var4) {
            var4 = var3;
            int var5 = var2;
            if (var1.contains(MapCollections.this.colGetEntry(var2, 1))) {
               MapCollections.this.colRemoveAt(var2);
               var5 = var2 - 1;
               var4 = var3 - 1;
               var6 = true;
            }

            var2 = var5 + 1;
         }

         return var6;
      }

      public boolean retainAll(Collection var1) {
         int var3 = MapCollections.this.colGetSize();
         boolean var6 = false;

         int var4;
         for(int var2 = 0; var2 < var3; var3 = var4) {
            var4 = var3;
            int var5 = var2;
            if (!var1.contains(MapCollections.this.colGetEntry(var2, 1))) {
               MapCollections.this.colRemoveAt(var2);
               var5 = var2 - 1;
               var4 = var3 - 1;
               var6 = true;
            }

            var2 = var5 + 1;
         }

         return var6;
      }

      public int size() {
         return MapCollections.this.colGetSize();
      }

      public Object[] toArray() {
         return MapCollections.this.toArrayHelper(1);
      }

      public Object[] toArray(Object[] var1) {
         return MapCollections.this.toArrayHelper(var1, 1);
      }
   }
}
