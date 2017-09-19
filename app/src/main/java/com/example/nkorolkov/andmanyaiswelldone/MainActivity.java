package com.example.nkorolkov.andmanyaiswelldone;

import android.content.Context;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final EditText textQuality = (EditText) findViewById(R.id.enter_quality);
        final EditText textScore = (EditText) findViewById(R.id.enter_score);
        final EditText textDescription = (EditText) findViewById(R.id.enter_description);

        final Button submitAdd = (Button) findViewById(R.id.submit_add_word);

        final ListView listItems = (ListView) findViewById(R.id.list_items);
        final ItemsAdapter adapter = new ItemsAdapter();
        listItems.setAdapter(adapter);

        submitAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Item item = new Item(
                        textQuality.getText().toString(),
                        Integer.valueOf(textScore.getText().toString()),
                        textDescription.getText().toString());
                adapter.add(item);

                textQuality.setText(""); textScore.setText(""); textDescription.setText("");
            }
        });
    }

    class Item {
        String quality, description;
        int score;

        Item(String quality, int score, String description){
            this.quality = quality;
            this.score = score;
            this.description = description;
        }
    }

    private class ItemsAdapter extends ArrayAdapter<Item> {
        public ItemsAdapter() {
            // context = MainActivity.this
            super(MainActivity.this, R.layout.item);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // перевод xml файла в объекты
            final View view = getLayoutInflater().inflate(R.layout.item, null);
            final Item item = getItem(position);
            assert item != null;
            ((TextView) view.findViewById(R.id.quality)).setText(item.quality);
            ((TextView) view.findViewById(R.id.score)).setText(String.valueOf(item.score));
            return view;
        }
    }
}
