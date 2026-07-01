# Crypto Tracker - Canlı Kripto Takip & Haber Uygulaması

Android üzerinde çalışan, Retrofit 2 ve CoinGecko API tabanlı anlık kripto para fiyat takip ve haber uygulaması. Bilişim Projesi dersi kapsamında geliştirilmiştir.

 Özellikler

* **Canlı Piyasası Takibi** — En popüler kripto para birimlerinin (Bitcoin, Ethereum, Solana vb.) anlık TRY fiyatları, sembolleri ve 24 saatlik değişim oranları canlı listelenir.
* **Dinamik Haber Banner'ı** — `ViewPager2` entegrasyonu ile ana sayfada kaydırılabilir güncel kripto haber başlıkları sunulur; tıklanıldığında tarayıcı üzerinden haber kaynağına yönlendirir.
* **Gelişmiş Favori Sistemi** — Kullanıcılar diledikleri kripto paraları favorilerine ekleyebilir. Bu veriler `SharedPreferences` ile yerel hafızaya kaydedilir.
* **Profil ve Favori Listesi** — Yalnızca kullanıcının favoriye eklediği kripto paraların filtrelenmiş olarak listelendiği özel profil ekranı.
* **Gece/Gündüz Modu (Dark Mode)** — Tek butonla dinamik tema değişimi; kullanıcının tema tercihi uygulama kapatılsa bile yerel hafızada saklanır ve otomatik yüklenir.
* **Firebase Kimlik Doğrulama** — Güvenli kayıt olma, giriş yapma ve şifre sıfırlama mekanizması.
* **Yaş Validasyonu** — Kayıt ekranında kullanıcının doğum yılı üzerinden otomatik yaş hesabı yapılarak 18 yaş sınırı kontrolü sağlanır.

 Yararlanılan Kaynaklar

* **Android Developer Dokümantasyonu** — Android API referansları, Java ve Android Jetpack bileşenleri.
* **CoinGecko API** — Ücretsiz kripto para piyasası canlı veri sağlayıcısı.
* **Firebase Authentication** — Kullanıcı kimlik doğrulama altyapısı.
* **Glide Kütüphanesi** — API'den gelen kripto logolarının asenkron olarak yüklenmesi ve önbelleğe alınması.
* **Retrofit 2 & GSON** — REST API haberleşmesi ve JSON veri serileştirme işlemleri.
* **ChatGPT & Claude**

## Kurulum ve API Yapılandırması

1. Projeyi bilgisayarınıza klonlayın ve Android Studio ile açın.
2. Firebase Console üzerinden bir proje oluşturun ve `google-services.json` dosyasını `app/` dizinine ekleyin.
3. Firebase panelinde **Email/Password** kimlik doğrulamasını aktif edin.
4. Uygulama Firebase mimarisi ve hazır CoinGecko endpoint'i (`https://api.coingecko.com/api/v3/`) ile doğrudan çalışmaya hazırdır.
