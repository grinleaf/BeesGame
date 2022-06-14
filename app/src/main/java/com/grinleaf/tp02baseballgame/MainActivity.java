package com.grinleaf.tp02baseballgame;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.grinleaf.tp02baseballgame.databinding.ActivityMainBinding;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    int chanceCount = 9;
    View view01, view02, view03= null;
    StringBuffer buffer= new StringBuffer();
    String s= null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        rndNum();

        //How To Play 버튼 기능
        binding.howToPlay.setOnClickListener(v -> howToPlay());

        //RESET 버튼 기능
        binding.reset.setOnClickListener(v -> hardReset());

        //각 bee 들이 클릭되었을 때
        binding.viewCard01.setOnClickListener(v -> selectBee(v));
        binding.viewCard02.setOnClickListener(v -> selectBee(v));
        binding.viewCard03.setOnClickListener(v -> selectBee(v));
        binding.viewCard04.setOnClickListener(v -> selectBee(v));
        binding.viewCard05.setOnClickListener(v -> selectBee(v));
        binding.viewCard06.setOnClickListener(v -> selectBee(v));
        binding.viewCard07.setOnClickListener(v -> selectBee(v));
        binding.viewCard08.setOnClickListener(v -> selectBee(v));
        binding.viewCard09.setOnClickListener(v -> selectBee(v));
        binding.viewCard10.setOnClickListener(v -> selectBee(v));
    }

    //벌이 클릭되었을 때 동작 메소드
    //1번 벌이 선택되었을 경우
    //bee01 카드뷰 invisible, flower01 뷰의 이미지를 flower02.png 로 set
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    void selectBee(View view) {
        if (chanceCount == 0) {
            Toast.makeText(this, "모든 기회를 사용하셨습니다. RESET 버튼을 눌러 다시 도전해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        //이미지비교 (3) : 이미지 비교를 위한 비트맵 변환 코드
        Bitmap flower01Bitmap01 = drawableToBitmap(binding.flower01.getDrawable());
        Bitmap flower01Bitmap02 = drawableToBitmap(binding.flower02.getDrawable());
        Bitmap flower01Bitmap03 = drawableToBitmap(binding.flower03.getDrawable());
        Drawable d = getResources().getDrawable(R.drawable.flower02, null);
        Bitmap flower02Bitmap01 = drawableToBitmap(d);

        if (sameAs(flower01Bitmap01, flower02Bitmap01)) {
            if (sameAs(flower01Bitmap02, flower02Bitmap01)) {
                if (sameAs(flower01Bitmap03, flower02Bitmap01)) {

                } else {
                    //이미지뷰 flower03 이 default 이미지일 때 실행
                    binding.flower03.setImageResource(R.drawable.flower02);
                    binding.selectFlower03Bee.setVisibility(View.VISIBLE);
                    view.setVisibility(View.INVISIBLE);
                    view03= view;

                    binding.confirm.setOnClickListener(v ->{
                        if(view01==null||view02==null||view03==null) {
                            softReset();
                            Toast.makeText(this, "세 마리의 벌을 모두 골라주세요.", Toast.LENGTH_SHORT).show();
                            chanceCount++;
                            return;
                        }
                        if(chanceCount>0) {
                            s += view.getTooltipText() + " 벌을 선택";
                            buffer.append(s + "\n");
                            binding.recordBoardTv.setText(buffer.toString());
                        }
                        //세 꽃에 모든 벌들이 앉았을 때 일벌인지, 맞는 자리인지 확인
                        //true -> checkFlowerPosition() 호출 -> true : backgroundcolor 파랑으로 set / false : backgroundcolor 초록으로 set
                        //false -> 선택된 뷰의 backgroundcolor 를 빨강으로 set, chanceCount 를 1회 감소, 선택된 벌의 visibility : visible
                        if (checkBee(view01, view02, view03)) {
                            if(checkFlowerPosition(view01)) view01.setBackgroundColor(this.getResources().getColor(R.color.card_strike, null));
                            if(checkFlowerPosition(view02)) view02.setBackgroundColor(this.getResources().getColor(R.color.card_strike, null));
                            if(checkFlowerPosition(view03)) view03.setBackgroundColor(this.getResources().getColor(R.color.card_strike, null));
                            softReset();

                            if(checkGoal()){
                                new AlertDialog.Builder(this)
                                        .setIcon(R.drawable.bee04)
                                        .setTitle("Bees Game")
                                        .setMessage("You Win!\n\n게임을 리셋합니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                hardReset();
                                                rndNum();
                                            }
                                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setIcon(R.drawable.bee04)
                                                .setTitle("Bees Game")
                                                .setMessage("게임을 종료합니다.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                }).create().show();
                                    }
                                }).create().show();
                                hardReset();
                            }
                        } else {
                            view01= null;
                            view02= null;
                            view03= null;
                            softReset();
                        }
                    });
                }
            } else {
                //이미지뷰 flower02 가 default 이미지일 때 실행
                binding.flower02.setImageResource(R.drawable.flower02);
                binding.selectFlower02Bee.setVisibility(View.VISIBLE);
                view.setVisibility(View.INVISIBLE);
                view02= view;
                s+= view.getTooltipText()+"  ";
            }
        } else {
            //이미지뷰 flower01 이 default 이미지일 때 실행
            binding.flower01.setImageResource(R.drawable.flower02);
            binding.selectFlower01Bee.setVisibility(View.VISIBLE);
            view.setVisibility(View.INVISIBLE);
            view01= view;
            s= (9-chanceCount+1)+" 번째 선택 : "+view.getTooltipText()+"  ";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Boolean checkGoal(){
        if(binding.tvFlower01.getText().equals(view01.getTooltipText())
                &&binding.tvFlower02.getText().equals(view02.getTooltipText())
                &&binding.tvFlower03.getText().equals(view03.getTooltipText())) {
            return true;
        }
        return false;
    }

    //선택된 벌이 일벌(ball)인지 확인하는 메소드
    @RequiresApi(api = Build.VERSION_CODES.O)
    Boolean checkBee(View view01, View view02, View view03) {
        if (binding.tvFlower01.getText().equals(view01.getTooltipText())
                || binding.tvFlower02.getText().equals(view01.getTooltipText())
                || binding.tvFlower03.getText().equals(view01.getTooltipText())){
            view01.setBackgroundColor(this.getResources().getColor(R.color.card_ball, null));
        }else view01.setBackgroundColor(this.getResources().getColor(R.color.card_out, null));

        if (binding.tvFlower01.getText().equals(view02.getTooltipText())
                || binding.tvFlower02.getText().equals(view02.getTooltipText())
                || binding.tvFlower03.getText().equals(view02.getTooltipText())){
            view02.setBackgroundColor(this.getResources().getColor(R.color.card_ball, null));
        }else view02.setBackgroundColor(this.getResources().getColor(R.color.card_out, null));

        if (binding.tvFlower01.getText().equals(view03.getTooltipText())
                || binding.tvFlower02.getText().equals(view03.getTooltipText())
                || binding.tvFlower03.getText().equals(view03.getTooltipText())){
            view03.setBackgroundColor(this.getResources().getColor(R.color.card_ball, null));
            return true;
        }else view03.setBackgroundColor(this.getResources().getColor(R.color.card_out, null));

        return false;
    }
    
    //일벌이 맞는 자리의 꽃에 앉았는지(Strike) 확인하는 메소드 : checkBee()의 결과가 true 일 경우 호출하기
    @RequiresApi(api = Build.VERSION_CODES.O)
    Boolean checkFlowerPosition(View view){
        if(view==view01&&binding.tvFlower01.getText().equals(view.getTooltipText())) return true;
        if(view==view02&&binding.tvFlower02.getText().equals(view.getTooltipText())) return true;
        if(view==view03&&binding.tvFlower03.getText().equals(view.getTooltipText())) return true;
        return false;
    }

    //세 개의 꽃 이미지만 default 로 돌아감 + chanceCount 차감
    void softReset(){
        binding.flower01.setImageResource(R.drawable.flower01);
        binding.flower02.setImageResource(R.drawable.flower01);
        binding.flower03.setImageResource(R.drawable.flower01);
        binding.selectFlower01Bee.setVisibility(View.INVISIBLE);
        binding.selectFlower02Bee.setVisibility(View.INVISIBLE);
        binding.selectFlower03Bee.setVisibility(View.INVISIBLE);
        binding.viewCard01.setVisibility(View.VISIBLE);
        binding.viewCard02.setVisibility(View.VISIBLE);
        binding.viewCard03.setVisibility(View.VISIBLE);
        binding.viewCard04.setVisibility(View.VISIBLE);
        binding.viewCard05.setVisibility(View.VISIBLE);
        binding.viewCard06.setVisibility(View.VISIBLE);
        binding.viewCard07.setVisibility(View.VISIBLE);
        binding.viewCard08.setVisibility(View.VISIBLE);
        binding.viewCard09.setVisibility(View.VISIBLE);
        binding.viewCard10.setVisibility(View.VISIBLE);
        if(chanceCount>0) {
            chanceCount--;
            binding.chanceCount.setText("총 "+chanceCount+"번의 기회");
        }
        else return;

    }

    //모든 게임상황이 default 로 돌아감
    @SuppressLint("ResourceAsColor")
    void hardReset() {
        binding.recordBoardTv.setText("");
        binding.flower01.setImageResource(R.drawable.flower01);
        binding.flower02.setImageResource(R.drawable.flower01);
        binding.flower03.setImageResource(R.drawable.flower01);
        binding.selectFlower01Bee.setVisibility(View.INVISIBLE);
        binding.selectFlower02Bee.setVisibility(View.INVISIBLE);
        binding.selectFlower03Bee.setVisibility(View.INVISIBLE);
        binding.viewCard01.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard02.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard03.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard04.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard05.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard06.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard07.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard08.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard09.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard10.setBackgroundColor(this.getResources().getColor(R.color.card_default, null));
        binding.viewCard01.setVisibility(View.VISIBLE);
        binding.viewCard02.setVisibility(View.VISIBLE);
        binding.viewCard03.setVisibility(View.VISIBLE);
        binding.viewCard04.setVisibility(View.VISIBLE);
        binding.viewCard05.setVisibility(View.VISIBLE);
        binding.viewCard06.setVisibility(View.VISIBLE);
        binding.viewCard07.setVisibility(View.VISIBLE);
        binding.viewCard08.setVisibility(View.VISIBLE);
        binding.viewCard09.setVisibility(View.VISIBLE);
        binding.viewCard10.setVisibility(View.VISIBLE);
        chanceCount = 9;
        binding.chanceCount.setText("총 " + chanceCount + "번의 기회");
        buffer.delete(0, buffer.length());
        view01 = null;
        view02 = null;
        view03 = null;
    }

    void howToPlay(){
        Intent intent= new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }

    //꽃의 tv 에 0~9까지의 랜덤한 번호 set (중복제거) (tv visibility : invisible)
    void rndNum(){

        int num[] = new int[3];
        Random rnd = new Random();
        for (int i = 0; i < num.length; i++) {
            num[i] = rnd.nextInt(10) + 1;
            for (int j = 0; j < i; j++) {
                if (num[i] == num[j]) i--;
            }
        }
        binding.tvFlower01.setText(num[0] + "");
        binding.tvFlower02.setText(num[1] + "");
        binding.tvFlower03.setText(num[2] + "");
    }

    //이미지비교 (1) : 이미지 --> 비트맵 변환 메소드
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    //이미지비교 (2) : 비트맵 비교 메소드
    private boolean sameAs(Bitmap bitmap1, Bitmap bitmap2) {

        ByteBuffer buffer1 = ByteBuffer.allocate(bitmap1.getHeight() * bitmap1.getRowBytes());
        bitmap1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(bitmap2.getHeight() * bitmap2.getRowBytes());
        bitmap2.copyPixelsToBuffer(buffer2);
        return Arrays.equals(buffer1.array(), buffer2.array());

    }

}