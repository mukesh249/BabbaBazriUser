package babbabazrii.com.bababazri.Activities;


import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Locale;

import babbabazrii.com.bababazri.Api.RequestCode;
import babbabazrii.com.bababazri.R;
import babbabazrii.com.bababazri.SharedPrefManager;

public class About extends AppCompatActivity {
    Toolbar toolbar_about;
    WebView about;
    String en_string="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "  <head>\n" +
            "  <title>Vendor Information</title>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
            "    \n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:300,400,700\" rel=\"stylesheet\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/open-iconic-bootstrap.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/animate.css\">\n" +
            "    \n" +
            "    <link rel=\"stylesheet\" href=\"css/owl.carousel.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/owl.theme.default.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/magnific-popup.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/aos.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/ionicons.min.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/bootstrap-datepicker.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/jquery.timepicker.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/flaticon.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/icomoon.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
            "  </head>\n" +
            "  <style type=\"text/css\">\n" +
            "  \tp{\n" +
            "  \t\tline-height: 29px;\n" +
            "    font-size: 16px;\n" +
            "        padding: 0px 25px;\n" +
            "    text-align: justify;\n" +
            "}\n" +
            "hr{\n" +
            "\tborder-top: 1px solid rgb(35, 77, 122);\n" +
            "\n" +
            "}\n" +
            "h3{\n" +
            "    padding-left: 2%;\n" +
            "    text-decoration: underline;\n" +
            "  }\n" +
            "  ul, ol{\n" +
            "    font-size: 16px;\n" +
            "  }\n" +
            "  ul li{\n" +
            "    list-style-type: none;\n" +
            "  }\n" +
            "  </style>\n" +
            "  <body>\n" +
            "   <div class=\"container\" style=\"padding: 5% 0%;\">\n" +
            "\n" +
            "   \t<h2 style=\"text-align: center; color: #234d7a; text-transform: uppercase;\" >           D V Babba Project Private Limited </h2>\n" +
            "   \t<hr>\n" +
            "   \n" +
            "   \t<h3>D V Babba Project Private Limited has Presented the Story of the Remote Areas:- </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "       There is a quirk of nature, which gives the benefit, the other one, the common tree or the milky cow. The man who annoys God too, the pass down to get the fruit from the mango tree breaks down to the common by giving the human stone, as if the cow is thirsty after milk, as a small child, hungry and thirsty He kills his own mother's feet, thereby hardening what life gives him only when it gets upset. It is also the most recent of rivers, quenched the thirst of people with serene water, and housing for people with venting soils, sand or stones, different types of people get their June 2 bread from mining work in the same area but so hard to After that, they also create high-rise buildings in big-big cities but have to mohtaj with facilities other than the June 2 bread for themselves, they cannot find the roof for the head, no clothes to wear. The arrival of commuters does not even have the facilities of commuting, what should be done by the people living in the area. For the basic amenities, the government also does not bring these areas a tempting opportunity in their budget, always in remote areas It is made to be a center of neglect, even if the chambal's rugged forest or the hope of a forest is formed.\n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "   \t<h3>Work in the Community Interest Made by D V Babba Project Pvt Ltd.</h3>\n" +
            "    <ul>\n" +
            "    <li>      We have come to you for a plan in which the development of these sectors will be achieved by Babba Project Pvt Ltd, which will also receive income from the gross profit of 30% of the money in the sector for growth. Education system will be strengthened in the areas, all citizens residing in these regions will be provided with opportunities for employment, as well as various infrastructure facilities such as housing facilities, medicine, high quality food, community building, means of traffic, poor They will be encouraged by giving children the education and scholarship for them. </li>\n" +
            "     \n" +
            "    </ul>\n" +
            "    <h2 style=\"text-align: center; color: #234d7a; text-transform: uppercase;\" >           Mission & Vision </h2>\n" +
            "    <hr>\n" +
            "   \n" +
            "    <h3>D V Babba Project Pvt Ltd The Basic Objective of the Company:-</h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        D V Babba Project Pvt. Ltd. The basic objective of building materials such as gravel, ballast, scree, dust, cement, water and so on, is easy to deliver to the consumer from producer to make the facilities available. These building materials will be available through us in a comfortable and affordable rates and we will provide home delivery.\n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "<h3>Contact Formula </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        To contact to D V Babba Project Pvt Ltd, You can contact at our customer service number 8377-830-830. Upon contacting this number, you may also choose your options such as gravel cement, our consumer service officer will contact you immediately.\n" +
            "\n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "\n" +
            "<h3>The Company will be Expanded in the Following Way:- </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "       Branch offices will be opened in different cities for the company's Golden future.</li>\n" +
            "\n" +
            "<li>For which the franchise holder will be able to manage field monitoring by marketing experts in different cities and the Commission will also be set by the marketing experts.</li>\n" +
            "\n" +
            "<li>The work of Company's franchise or dealership will first be started with Tonk, Sawai Madhopur, Dausa, and Jaipur.\n" +
            "\n" +
            "</li>\n" +
            "\n" +
            "<li>The basic objective of franchise is to provide the information to common man about the facilities that the company provides and to explain to the drivers and the vehicle owners about the original purposes.</li>\n" +
            "\n" +
            "\n" +
            "     \n" +
            "    </ul>\n" +
            "   \t<hr>\n" +
            "   </div>\n" +
            "  </body>\n" +
            "  </html>";
    String hi_string="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "  <head>\n" +
            "  <title>Vendor Information</title>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
            "    \n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:300,400,700\" rel=\"stylesheet\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/open-iconic-bootstrap.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/animate.css\">\n" +
            "    \n" +
            "    <link rel=\"stylesheet\" href=\"css/owl.carousel.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/owl.theme.default.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/magnific-popup.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/aos.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/ionicons.min.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"css/bootstrap-datepicker.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/jquery.timepicker.css\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/flaticon.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/icomoon.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
            "  </head>\n" +
            "  <style type=\"text/css\">\n" +
            "  \tp{\n" +
            "  \t\tline-height: 29px;\n" +
            "    font-size: 16px;\n" +
            "        padding: 0px 25px;\n" +
            "    text-align: justify;\n" +
            "}\n" +
            "hr{\n" +
            "\tborder-top: 1px solid rgb(35, 77, 122);\n" +
            "\n" +
            "}\n" +
            "h3{\n" +
            "    padding-left: 2%;\n" +
            "    text-decoration: underline;\n" +
            "  }\n" +
            "  ul, ol{\n" +
            "    font-size: 16px;\n" +
            "  }\n" +
            "  ul li{\n" +
            "    list-style-type: none;\n" +
            "  }\n" +
            "  </style>\n" +
            "  <body>\n" +
            "   <div class=\"container\" style=\"padding: 5% 0%;\">\n" +
            "\n" +
            "   \t<h2 style=\"text-align: center; color: #234d7a; text-transform: uppercase;\" >           D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड </h2>\n" +
            "   \t<hr>\n" +
            "   \n" +
            "   \t<h3> D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड की ओर से प्रस्तुत है सुदूर इलाकों की कहानी :- </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        प्रकृति की लीला ही विचित्र है जो लाभ देता है, उसे ही दूसरे परेशान करते हैं ,चाहे वह आम का पेड़ हो अथवा दूध देने वाली गाय। देने वाले को भगवान भी परेशान करता है ,आम के पेड़ से फल प्राप्त करने के लिए नीचे बैठा राहगीर मानव उसके पत्थर देकर आम को तोड़ता है, जैसे कि गाय को दूध देने के बाद उसको तरह-तरह की पीड़ा देता है,जब एक छोटे बच्चे की भूख और प्यास लगती प्यास लगती है तो वह अपने ही मां के पैर मारता है , जिससे सख्त हो जाता है कि जो जीवन देता है उसे ही परेशान किया जाता है । नदियों का भी यही हाल है उसके निर्मल जल से लोगों की प्यास बुझती है और निकलने वाली मिट्टी,बालू अथवा पत्थर से लोगों के लिए आवास बनते हैं ,विभिन्न प्रकार के लोगों को अपनी 2 जून की रोटी भी इन्हीं क्षेत्र में खनन कार्य करने से मिल ही जाती है लेकिन इतनी मेहनत करने के उपरांत भी बड़े-बड़े शहरों में ऊंची ऊंची इमारतें तो बना देते हैं लेकिन खुद के लिए 2 जून की रोटी के अलावा अन्य सुविधाओं से मोहताज रहना पड़ता है, उन्हें सिर ढकने के लिए छत नहीं मिल पाती, पहनने के लिए कपड़े नहीं मिल पाते ,आने जाने के लिए आवागमन की सुविधाएं भी नहीं मिल पाती, क्षेत्र में रहने वाले लोगों के द्वारा किया जाए भी तो क्या किया जाए ।मूलभूत सुविधाओं के लिए सरकार भी इन क्षेत्रों को अपने बजट में कोई लुभावना अवसर नहीं ला पाती, हमेशा ही सुदूर  क्षेत्रों को उपेक्षा का केंद्र बना दिया जाता है चाहे चंबल का बीहड़ या फिर वन की आशा कही जाने वाली नदी बनास हो। \n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "   \t<h3>D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड के द्वारा किए जाने वाले समाज हित में कार्य ।</h3>\n" +
            "    <ul>\n" +
            "    <li>       हम आपके लिए लेकर आए हैं एक ऐसी योजना जिसमें कि इन्हीं क्षेत्रों का विकास किया जाएगा ,बाबा प्रोजेक्ट प्राइवेट लिमिटेड की तरफ से जो भी आय प्राप्त होगी उसमे से सकल लाभ में से 30% पैसा क्षेत्र में विकास के लिए लगाई जाएगी। क्षेत्रो में शिक्षा व्यवस्था को मजबूत बनाया जाएगा ,इन क्षेत्र में रहने वाले समस्त नागरिकों को रोजगार के अवसर अवसर प्रदान किए जाएंगे ,साथ ही विभिन्न प्रकार की मूलभूत सुविधाएं जैसे की आवास की सुविधा , चिकित्सा ,उच्च गुणवत्ता युक्त भोजन,सामुदायिक भवन ,आवागमन के साधन, गरीब बच्चों के शिक्षा,एवं उनके लिए छात्रवृत्ति देकर उन को प्रोत्साहित किया जाएगा । </li>\n" +
            "     \n" +
            "    </ul>\n" +
            "    <h2 style=\"text-align: center; color: #234d7a; text-transform: uppercase;\" >           मिशन & विज़न </h2>\n" +
            "    <hr>\n" +
            "   \n" +
            "    <h3> D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड कंपनी का मूल उद्देश्य:- </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड का मूल उद्देश्य बिल्डिंग मैटेरियल्स जैसे कि बजरी, गिट्टी, रोडी,डस्ट,सीमेंट,पानी जैसे  अन्य  सामान को उत्पादक से उपभोक्ता तक पहुंचाने की आसान सुविधा उपलब्ध करवाना है . उक्त बिल्डिंग मटेरियल हमारे माध्यम से सहज एवं सस्ती दर में उपलब्ध हो जाएगा तथा हम होम डिलीवरी प्रदान करेंगे.\n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "<h3>संपर्क सूत्र </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        D V बाबा प्रोजेक्ट प्राइवेट लिमिटेड के लिए संपर्क के लिए आप आप हमारे ग्राहक सेवा नंबर 8377-830-830पर कर सकते हैं.\n" +
            "       इस नंबर पर संपर्क करने पर आप अपना विकल्प जैसे की बजरी सीमेंट का भी चयन कर सकते हैं हमारे उपभोक्ता सेवा अधिकारी आपसे तुरंत संपर्क करेंगे.\n" +
            "\n" +
            "</li>\n" +
            "     \n" +
            "    </ul>\n" +
            "\n" +
            "<h3>कंपनी का विस्तार निम्न तरीके से किया जाएगा:- </h3>\n" +
            "    <ul>\n" +
            "    <li>\n" +
            "        कंपनी के सुनहरे भविष्य के लिए विभिन्न शहरों में ब्रांच ऑफिस खोली जाएगी |</li>\n" +
            "\n" +
            "<li>जिसके लिए विभिन्न शहरों में मार्केटिंग एक्सपर्ट के द्वारा फील्ड की मॉनिटरिंग कर फ्रेंचाइजी होल्डर का चयन किया जाएगा एवं मार्केटिंग एक्सपर्ट के द्वारा उसका कमीशन भी तय किया जाएगा |</li>\n" +
            "\n" +
            "<li>कंपनी की फ्रेंचाइजी अथवा डीलरशिप देने का कार्य सबसे पहले टोंक, सवाई माधोपुर, दोैसा, एवम् जयपुर से प्रारंभ किया जाएगा |</li>\n" +
            "\n" +
            "<li>फ्रेंचाइजी देने का मूल उद्देश्य आम आदमी को कंपनी के द्वारा दी जाने वाली सुविधाओं के बारे में जानकारी उपलब्ध करवाना एवं वाहन चालक को एवं वाहन मालिकों को मूल उद्देश्य के बारे में समझाना है |</li>\n" +
            "\n" +
            "\n" +
            "     \n" +
            "    </ul>\n" +
            "   \t<hr>\n" +
            "   </div>\n" +
            "  </body>\n" +
            "  </html>";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);
        toolbar_about = (Toolbar)findViewById(R.id.toolbar_about);
        setSupportActionBar(toolbar_about);
        getSupportActionBar().setTitle(R.string.about);

        about = (WebView)findViewById(R.id.about);
        about.getSettings().setJavaScriptEnabled(true);

        if (SharedPrefManager.getLangId(About.this, RequestCode.LangId).compareTo("hi")==0){
            about.loadData(hi_string, "text/html", "UTF-8");
        }else {
            about.loadData(en_string, "text/html", "UTF-8");
        }

        //toolbar back button color and icon change
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_black);
        upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SharedPrefManager.getIslagChange(About.this)) {
            if (SharedPrefManager.getLangId(About.this,RequestCode.LangId).compareTo("") != 0) {
                setLangRecreate(SharedPrefManager.getLangId(About.this,RequestCode.LangId));
            } else {
                Toast.makeText(About.this, R.string.something_wrong,Toast.LENGTH_LONG).show();
            }
        }
    }
    public void setLangRecreate(String langval) {
        Locale locale;
        Configuration config = getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        SharedPrefManager.setLangId(About.this,RequestCode.LangId, langval);
    }

}

