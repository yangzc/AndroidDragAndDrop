# AndroidDragAndDrop
--------------------
A drag and drop widget

### Code Examples
```
<com.hyean.dd.DragAndDropLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:id="@+id/dd"
       android:layout_height="match_parent">
   
       <LinearLayout
           android:id="@+id/top"
           android:layout_width="match_parent"
           android:layout_height="wrap_content"
           android:orientation="horizontal">
   
           <com.hyena.dd.samples.DraggableButton
               android:id="@+id/btn_1"
               android:layout_width="0dp"
               android:layout_height="wrap_content"
               android:layout_weight="1"
               android:text="draggable_1" />
   
           <com.hyena.dd.samples.DraggableButton
               android:id="@+id/btn_2"
               android:layout_width="0dp"
               android:layout_height="wrap_content"
               android:layout_weight="1"
               android:text="draggable_2" />
   
           <com.hyena.dd.samples.DraggableButton
               android:id="@+id/btn_3"
               android:layout_width="0dp"
               android:layout_height="wrap_content"
               android:layout_weight="1"
               android:text="draggable_3" />
       </LinearLayout>
   
       <com.hyena.dd.samples.DroppableButton
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:id="@+id/drop"
           android:layout_below="@id/top"
           android:layout_centerHorizontal="true"
           android:layout_marginTop="20dp"
           android:text="droppable" />
   </com.hyean.dd.DragAndDropLayout>
   ```
   
