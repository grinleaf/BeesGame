package com.grinleaf.tp02baseballgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.grinleaf.tp02baseballgame.databinding.ActivityDialogBinding;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity {

    ActivityDialogBinding binding;
    ArrayList<String> howToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        howToPlay= new ArrayList<String>();
        howToPlay.add("열 마리의 벌 중 세 마리의 일벌을\n\n찾아야합니다.");
        howToPlay.add("일벌은 1,2,3번 꽃 중 각자 정해진 자리에만\n\n앉을 수 있습니다.");
        howToPlay.add("\n일벌 O + 알맞은 자리의 꽃 - 파랑\n\n일벌 O +  다른 자리의 꽃 - 초록\n\n일벌 X - 빨강 배경으로 표시됩니다.\n");
        howToPlay.add("\n주어진 기회는 총 9번입니다.\n\n9번 안에 일벌들의 자리를 찾아주세요!\n");

        binding.dialogTitle.setText("How To Play");
        binding.dialogMessage.setText(howToPlay.get(0));
        binding.dialogTvPrev.setVisibility(View.GONE);
        binding.dialogTvPrev.setOnClickListener(v->clickPrev());
        binding.dialogTvNext.setOnClickListener(v->clickNext());
    }

    int dialogPage=0;

    void clickPrev(){
        if(dialogPage==0) {
            binding.dialogTvPrev.setVisibility(View.GONE);
            return;
        }
        dialogPage--;
        binding.dialogMessage.setText(howToPlay.get(dialogPage));
    }

    void clickNext(){
        binding.dialogTvPrev.setVisibility(View.VISIBLE);
        dialogPage++;
        if(binding.dialogMessage.getText().equals(howToPlay.get(2))){
            binding.dialogTvNext.setText("확인");
            binding.dialogTvNext.setTextColor(getResources().getColor(R.color.default_color,null));
            binding.dialogTvPrev.setVisibility(View.GONE);
            binding.dialogMessage.setText(howToPlay.get(dialogPage));
        }else if(binding.dialogMessage.getText().equals(howToPlay.get(3))){
            binding.dialogTvPrev.setVisibility(View.GONE);
            finish();
        }else{
            binding.dialogMessage.setText(howToPlay.get(dialogPage));
        }
    }
}


//*************** AlertDialog 단순 중첩 시 다이얼로그 전환마다 깜빡임 발생 --> 액티비티로 다이얼로그 화면 생성 *****************
//        AlertDialog.Builder builder1= new AlertDialog.Builder(this);
//        builder1.setTitle("How To Play");
//        builder1.setIcon(R.drawable.bee04);
//        builder1.setMessage("\n열 마리의 벌 중 세 마리의 일벌을 찾아야합니다.\n");
//        builder1.setPositiveButton("다음", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("How To Play")
//                        .setIcon(R.drawable.bee04)
//                        .setMessage("\n일벌은 1,2,3번 꽃 중 각자 정해진 자리에만\n\n앉을 수 있습니다.")
//                        .setPositiveButton("다음", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new AlertDialog.Builder(MainActivity.this)
//                                        .setTitle("How To Play")
//                                        .setIcon(R.drawable.bee04)
//                                        .setMessage("\n일벌 O + 알맞은 자리의 꽃 - 파랑\n\n일벌 O +  다른 자리의 꽃 - 초록\n\n일벌 X - 빨강 배경으로 표시됩니다.")
//                                        .setPositiveButton("다음", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                new AlertDialog.Builder(MainActivity.this)
//                                                        .setTitle("How To Play")
//                                                        .setIcon(R.drawable.bee04)
//                                                        .setMessage("\n주어진 기회는 총 9번입니다.\n\n9번 안에 일벌들의 자리를 찾아주세요!")
//                                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                            }
//                                                        }).create().show();
//                                            }
//                                        }).create().show();
//                            }
//                        }).create().show();
//            }
//        });
//        builder1.create().show();