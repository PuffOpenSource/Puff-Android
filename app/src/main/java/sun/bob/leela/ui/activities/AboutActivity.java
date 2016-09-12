package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import sun.bob.leela.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element i8Element = new Element("icons8", "Icons from icons8.com",mehdi.sakout.aboutpage.R.drawable.about_icon_link);
        i8Element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://icons8.com"));
                startActivity(i);
            }
        });

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_puff)
                .setDescription(getString(R.string.about_puff))
                .addGroup("Connect with us")
                .addEmail("bob.sun@outlook.ie")
                .addWebsite("http://bobsun.website/")
                .addGitHub("SpongeBobSun")
                .addItem(i8Element)
                .addGroup("About the name")
                .addGroup("Blowfish Puffs!")
                .create();

        setContentView(aboutPage);
    }

    private Element getBcryptElement() {
        Element ret = new Element();
        ret.setTitle(getString(R.string.about_bcrypt));
        return ret;
    }
}
