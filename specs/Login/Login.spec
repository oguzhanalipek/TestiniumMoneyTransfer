Login
=====

Başarılı Login
--------------
tags: smokeTest
* Catchylabs login ekraninin acildigi dogrulanir
* Username alanina "oguzhan.alipek" bilgisi girilir
* Password alanina "Oa426336" bilgisi girilir
* Login butonuna tiklanir
* Uygulamaya giris yapildigi dogrulanir

Kullanici Adi ve Şifre Alanları Boş Bırakılarak Login Olmaya Çalışıldığında Uyarı Mesajinin Görüntülenmesi
----------------------------------------------------------------------------------------------------------
* Catchylabs login ekraninin acildigi dogrulanir
* Username input alani temizlenir
* Password input alani temizlenir
* Login butonuna tiklanir
* Login ekraninda "Username or Password Invalid!" icerikli hata mesajinin goruntulendigi dogrulanir

Geçersiz Kullanici Adi ve Şifre ile Login Olmaya Çalışıldığında Uyarı Mesajinin Görüntülenmesi
----------------------------------------------------------------------------------------------
* Catchylabs login ekraninin acildigi dogrulanir
* Username alanina "gecersiz" bilgisi girilir
* Password alanina "gecersiz" bilgisi girilir
* Login butonuna tiklanir
* Login ekraninda "Username or Password Invalid!" icerikli hata mesajinin goruntulendigi dogrulanir

Başarılı Logout
---------------
tags: smokeTest
* Catchylabs login ekraninin acildigi dogrulanir
* Username alanina "oguzhan.alipek" bilgisi girilir
* Password alanina "Oa426336" bilgisi girilir
* Login butonuna tiklanir
* Uygulamaya giris yapildigi dogrulanir
* Logout butonuna tiklanir
* Catchylabs login ekraninin acildigi dogrulanir