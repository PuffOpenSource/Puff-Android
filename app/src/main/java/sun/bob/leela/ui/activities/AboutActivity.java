package sun.bob.leela.ui.activities;

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

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_puff)
                .setDescription(getString(R.string.about_puff))
                .addGroup("About Bcrypt")
                .addItem(getBcryptElement())
                .addWebsite("http://bcrypt.sourceforge.net/")
                .addGroup("Connect with us")
                .addEmail("bob.sun@outlook.ie")
                .addWebsite("http://bobsun.website/")
                .addGitHub("SpongeBobSun")
                .addGroup("About the name")
                .addItem(new Element().setTitle("Blow fish Puffs!"))
                .create();

        setContentView(aboutPage);
    }

    private Element getBcryptElement() {
        Element ret = new Element();
        ret.setTitle(getString(R.string.about_bcrypt));
        return ret;
    }
}
