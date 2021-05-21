package com.example.mqttenlace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView tv_message;
    private static final String TAG = "MainActivity";
    private String topic, clientID, port;
    private MqttAndroidClient clientA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        btn = findViewById(R.id.btn_obtener);
        tv_message = findViewById(R.id.tv_message);
        clientID = "xxx";
        port = "8000";
        topic = "testtopic/probando";

        clientA = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                clientID);
        // conect2();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectX();
            }
        });
    }

    private void conectX(){
        try {
            IMqttToken token = clientA.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    private void sub(){
        try {
            clientA.subscribe(topic, 0);
            clientA.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    tv_message.setText("Conexion Perdida");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "Topic "+ topic);
                    tv_message.setText("");
                    tv_message.setText(new String(message.getPayload()));
                    Log.d(TAG, "Message" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

        }catch (MqttException e){

        }
}}