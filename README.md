## 📚 Sistem Manajemen Perpustakaan - Java OOP

Sistem ini merupakan implementasi program manajemen perpustakaan berbasis GUI menggunakan **JavaFX** dan konsep **Pemrograman Berorientasi Objek (OOP)** dalam bahasa Java.

---

### 📁 Struktur Folder & Kelas

```

Management-Perpustakaan/
├── app/
│   └── src/
│   |   └── main/
│   |       ├── java/
│   |       │   └── management.perpustakaan/
│   |       │       ├── App.java                 // Main Application
│   |       │       ├── HomeController.java      // Controller untuk Home
│   |       │       ├── BookController.java      // Controller untuk Buku
│   |       │       ├── MemberController.java    // Controller untuk Anggota
│   |       │       └── models/
│   |       │           ├── Book.java           // Model Buku
│   |       │           ├── Member.java         // Model Anggota
│   |       │           ├── Library.java        // Model Perpustakaan
│   |       │           └── LibraryItem.java    // Abstract Model Item
│   |       └── resources/
│   |           └── management.perpustakaan/
│   |               ├── HomeView.fxml          // UI Home Page
│   |               ├── BookView.fxml          // UI Manajemen Buku
│   |               └── MemberView.fxml        // UI Manajemen Anggota
|   |
|   |
|   └──books.json                // Data buku
|   └──members.json              // Data anggota
|   └──build.gradle              // Konfigurasi Gradle (jika ada)
```

---

### 📦 Penjelasan Tiap Kelas

| Kelas                | Deskripsi                                                                                                 |
|----------------------|-----------------------------------------------------------------------------------------------------------|
| `LibraryItem`        | Kelas abstrak untuk item perpustakaan (buku, dll).                                                        |
| `Book`               | Turunan dari `LibraryItem`, mewakili buku dengan atribut judul, penulis, status pinjam, dsb.              |
| `Member`             | Mewakili anggota perpustakaan, menyimpan data nama, ID, aktivitas, dan daftar buku yang dipinjam.         |
| `Library`            | Menyimpan koleksi item dan anggota, serta menyediakan logika peminjaman/pengembalian.                     |
| `LibraryLogger`      | Mencatat aktivitas penting dalam sistem.                                                                  |
| `App`                | Entry point aplikasi JavaFX, mengatur scene dan inisialisasi data.                                        |
| `BookController`     | Controller untuk tampilan dan logika manajemen buku (tambah, edit, hapus, pinjam, kembali).               |
| `MemberController`   | Controller untuk tampilan dan logika manajemen anggota.                                                   |
| `HomeController`     | Controller untuk tampilan menu utama.                                                                     |
| `PersistenceService` | Menyimpan dan memuat data ke/dari file JSON (`books.json`, `members.json`).                               |

---

### 🧠 Konsep OOP yang Digunakan

* **Abstraksi**: `LibraryItem` sebagai superclass abstract
```java
public abstract class LibraryItem {
    public String title;
    public int itemId;
    public boolean isBorrowed;
    ...
}
```

* **Inheritance**: `Book` mewarisi dari `LibraryItem`.

```java
public class Book extends LibraryItem {
    public String author;
    ...
}
```
* **Encapsulation**: Atribut privat, akses via getter/setter

```java
public class Member extends Person implements MemberOperations {
    ...
    private List<Integer> borrowedBookIds;
    ...
    public void setBorrowedBookIds(List<Integer> ids) {
        this.borrowedBookIds = ids;
    }
    ...
}

```
* **Polymorphism**: Override method pada Library.java
```java
public abstract String getDescription();

```

Kemudian pada Book.java
```java
@Override
    public String getDescription() {
        return "Buku: " + getTitle() + " oleh " + author + ", ID: " + getItemId();
    }
```

---

### 👨‍💼 Fitur Admin/Operator

* Tambah, edit, hapus buku
* Lihat daftar buku & status pinjam
* Tambah, edit, hapus anggota
* Lihat daftar anggota & aktivitas
* Peminjaman & pengembalian buku

---


### 🧾 Contoh Data Anggota (JSON)

```json
[
  {
    "memberId": "M001",
    "name": "Budi pekerti",
    "activities": ["Anggota telah terdaftar: M001"],
    "borrowedBookIds": []
  }
]
```

---

### ▶️ Cara Menjalankan Program

# Panduan Menjalankan Aplikasi Manajemen Perpustakaan

Aplikasi desktop sederhana untuk mengelola data buku dan anggota perpustakaan, lengkap dengan fitur peminjaman, pengembalian, dan penyimpanan data otomatis menggunakan file JSON.

## 1. Prasyarat (Perangkat Lunak yang Dibutuhkan)

Sebelum memulai, pastikan perangkat lunak berikut sudah terpasang di komputer Anda:

* **Git**: Digunakan untuk mengunduh kode sumber proyek dari repositori.
* **Java Development Kit (JDK)**: Pastikan Anda memiliki JDK versi 21 . Jika belum punya, kami merekomendasikan untuk mengunduhnya terlebih dahulu.

## 2. Langkah-langkah Instalasi dan Menjalankan

Ikuti langkah-langkah ini secara berurutan untuk menjalankan aplikasi.

### Langkah 1: Unduh Kode Proyek (Clone Repository)

1. Buka terminal atau command prompt Anda (di Windows, bisa menggunakan Command Prompt, Powershell, atau Git Bash).
2. Jalankan perintah berikut. 

   ```bash
   https://github.com/HA141User/Management-Perpustakaan
   ```

   Contoh:

   ```bash
   git clone https://github.com/username/Management-Perpustakaan.git
   ```

   Perintah ini akan membuat sebuah folder baru bernama `Management-Perpustakaan` yang berisi semua file proyek.

### Langkah 2: Masuk ke Folder Proyek

Gunakan perintah `cd` (change directory) untuk masuk ke dalam folder yang baru saja dibuat.

```bash
cd Management-Perpustakaan
```

### Langkah 3: Jalankan Aplikasi Menggunakan Gradle

Proyek ini menggunakan **Gradle Wrapper** (`gradlew`), yang berarti Anda tidak perlu menginstal Gradle secara manual. Gradle Wrapper akan secara otomatis mengunduh semua yang dibutuhkan (termasuk library JavaFX dan Gson).

1. Pastikan Anda berada di dalam folder `Management-Perpustakaan`, lalu jalankan perintah yang sesuai dengan sistem operasi Anda:

   #### Untuk pengguna Windows:

   ```bash
   .\gradlew run
   ```

   #### Untuk pengguna macOS atau Linux:

   ```bash
   ./gradlew run
   ```

### Catatan Penting:

* Saat pertama kali dijalankan, Gradle akan mengunduh banyak file (disebut dependencies). Proses ini mungkin memakan waktu beberapa menit tergantung koneksi internet Anda. Harap bersabar dan biarkan prosesnya selesai.


---

**Jumlah Anggota:** 3 Orang  
**Nama Kelompok:** Kelompok 22

| No. | Nama                    | Tugas                                                                                                                |
|-----|-------------------------|----------------------------------------------------------------------------------------------------------------------|
| 1.  | Hilmy Affayyad Akbar     | Mengelola daftar buku: menampilkan nama buku, ID, status peminjaman (termasuk ID anggota yang meminjam), serta tombol tambah, edit, dan hapus buku. |
| 2.  | Maisyah Mahdiyyah       | Mengelola daftar anggota: menampilkan ID dan nama anggota, tombol tambah, edit, hapus, bersihkan daftar anggota, serta fitur simpan/muat data JSON. |
| 3.  | Muhammad Hairi          | Mengelola halaman utama: menampilkan tombol navigasi ke daftar anggota dan daftar buku, serta judul "Management Perpustakaan". |

---

### 👨‍💻 Dibuat Oleh

* **Kelompok 22**
* **Kelas / Prodi**: Kelas A / Sistem Informasi
* **Mata Kuliah**: Pemrograman Berorientasi Objek
* **Anggota**:  
  * Muhammad Hairi
  * Hilmy Affayyad Akbar  
  * Maisyah Mahdiyyah
