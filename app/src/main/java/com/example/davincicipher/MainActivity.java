package com.example.davincicipher;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final String[] titles = {
        "ด่าน 1 · รหัสซีซาร์",
        "ด่าน 2 · ตัวเลขแทนอักษร",
        "ด่าน 3 · ลำดับสัญลักษณ์",
        "ด่าน 4 · ข้อความที่ซ่อนอยู่",
        "ด่าน 5 · รหัสสุดท้าย"
    };
    private final String[] puzzles = {
        "ถอดรหัสข้อความนี้: FDHVDU\nคำใบ้: เลื่อนตัวอักษรภาษาอังกฤษย้อนกลับ 3 ตำแหน่ง",
        "ถอดตัวเลขเป็นตัวอักษร (A=1, B=2, ...): 4-1-22-9-14-3-9",
        "เรียงลำดับตัวอักษรตามตัวเลขกำกับ: 3=ก, 1=ร, 4=ด, 2=ห",
        "อ่านข้อความแล้วหาคำที่ซ่อนอยู่:\n“ดาวเหนือส่องทางในคืนมืด”\nคำใบ้: มองหาสิ่งที่ใช้บอกทิศ",
        "รหัสสุดท้าย: 20-8-5-32-11-5-25\nคำใบ้: A=1 ถึง Z=26 และ 32 คือเว้นวรรค"
    };
    private final String[] answers = {"CAESAR", "DAVINCI", "รหัสดก", "เหนือ", "THE KEY"};
    private final String[] hints = {
        "ตัวอักษร C ถอยหลัง 3 ตำแหน่งจะเป็น Z",
        "แปลง 1=A, 2=B ไปเรื่อย ๆ",
        "อ่านตามเลขจากน้อยไปมาก",
        "คำตอบเป็นทิศหนึ่งในสี่ทิศ",
        "ถอดเลขเป็นตัวอักษรทีละตัว"
    };
    private int level = 0;
    private int score = 0;
    private int hintsUsed = 0;
    private TextView progress;
    private TextView puzzle;
    private TextView feedback;
    private TextView scoreView;
    private EditText answerInput;
    private LinearLayout root;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showGame();
    }

    private GradientDrawable panel(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(28);
        d.setStroke(2, stroke);
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(Color.rgb(30, 23, 14));
        b.setAllCaps(false);
        b.setTextSize(16);
        b.setBackground(panel(Color.rgb(205, 165, 83), Color.rgb(235, 204, 137)));
        return b;
    }

    private void showGame() {
        int bg = Color.rgb(24, 21, 17);
        int paper = Color.rgb(45, 38, 28);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22, 28, 22, 24);
        root.setBackgroundColor(bg);

        ScrollView scroll = new ScrollView(this);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER_HORIZONTAL);
        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        TextView brand = text("THE LOST MANUSCRIPT", 13, Color.rgb(205,165,83), true);
        content.addView(brand, space(48));
        TextView heading = text("DA VINCI CIPHER", 29, Color.rgb(242,224,184), true);
        content.addView(heading, space(58));
        TextView subtitle = text("ปริศนาลับแห่งต้นฉบับโบราณ", 16, Color.rgb(194,177,145), false);
        content.addView(subtitle, space(42));

        progress = text("", 14, Color.rgb(205,165,83), true);
        content.addView(progress, space(42));

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 22, 20, 22);
        card.setBackground(panel(paper, Color.rgb(116,91,52)));
        content.addView(card, new LinearLayout.LayoutParams(-1, -2));

        TextView levelTitle = text(titles[level], 20, Color.rgb(242,224,184), true);
        card.addView(levelTitle, space(48));
        puzzle = text(puzzles[level], 18, Color.rgb(235,220,190), false);
        puzzle.setGravity(Gravity.CENTER);
        card.addView(puzzle, space(120));

        answerInput = new EditText(this);
        answerInput.setSingleLine(true);
        answerInput.setTextColor(Color.WHITE);
        answerInput.setHintTextColor(Color.rgb(174,157,128));
        answerInput.setHint("พิมพ์คำตอบที่นี่");
        answerInput.setTextSize(17);
        answerInput.setPadding(18, 12, 18, 12);
        answerInput.setBackground(panel(Color.rgb(31,27,21), Color.rgb(116,91,52)));
        card.addView(answerInput, space(56));

        Button submit = button("ตรวจคำตอบ");
        card.addView(submit, space(54));
        submit.setOnClickListener(v -> checkAnswer());

        Button hint = button("ขอคำใบ้  (หัก 5 คะแนน)");
        card.addView(hint, space(52));
        hint.setOnClickListener(v -> {
            if (hintsUsed >= 5) {
                Toast.makeText(this, "ใช้คำใบ้ครบแล้ว", Toast.LENGTH_SHORT).show();
                return;
            }
            hintsUsed++;
            score = Math.max(0, score - 5);
            Toast.makeText(this, "คำใบ้: " + hints[level], Toast.LENGTH_LONG).show();
            scoreView.setText("คะแนน: " + score);
        });

        feedback = text("", 15, Color.rgb(231,193,110), false);
        card.addView(feedback, space(50));

        scoreView = text("คะแนน: " + score, 16, Color.rgb(205,165,83), true);
        content.addView(scoreView, space(48));
        TextView footer = text("ห้องสมุดลับ · บันทึกความคืบหน้าในรอบนี้", 12, Color.rgb(125,111,89), false);
        content.addView(footer, space(38));
        setContentView(root);
    }

    private LinearLayout.LayoutParams space(int height) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, height);
        p.bottomMargin = 12;
        return p;
    }

    private void checkAnswer() {
        String entered = answerInput.getText().toString().trim();
        if (entered.equalsIgnoreCase(answers[level])) {
            score += 20;
            if (level == 4) {
                showVictory();
            } else {
                level++;
                showGame();
                Toast.makeText(this, "ถูกต้อง! ปลดล็อกด่านถัดไปแล้ว", Toast.LENGTH_SHORT).show();
            }
        } else {
            feedback.setText("ยังไม่ถูกต้อง ลองสังเกตเบาะแสอีกครั้ง");
            feedback.setTextColor(Color.rgb(220,111,86));
        }
    }

    private void showVictory() {
        LinearLayout win = new LinearLayout(this);
        win.setOrientation(LinearLayout.VERTICAL);
        win.setGravity(Gravity.CENTER);
        win.setPadding(28, 28, 28, 28);
        win.setBackgroundColor(Color.rgb(24,21,17));
        TextView title = text("ปริศนาถูกคลี่คลาย", 28, Color.rgb(235,198,116), true);
        win.addView(title, space(70));
        win.addView(text("คุณค้นพบความลับแห่งต้นฉบับโบราณแล้ว", 18, Color.WHITE, false), space(70));
        win.addView(text("คะแนนรวม  " + score, 22, Color.rgb(235,198,116), true), space(60));
        Button again = button("เล่นอีกครั้ง");
        win.addView(again, space(56));
        again.setOnClickListener(v -> { level = 0; score = 0; hintsUsed = 0; showGame(); });
        setContentView(win);
    }
}
