<template>
  <div class="home">
    <div class="parallax">
      <div class="overlay"></div>
      <div class="content">
        <h1>{{ welcomeMessage }}</h1>
        <p>{{ followMessage }}</p>
        <div class="buttons">
          <router-link to="/news" class="btn">Haberler</router-link>
          <router-link to="/announcements" class="btn">Duyurular</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      welcomeMessage: '',
      followMessage: '',
      originalWelcomeMessage: 'Derneğimize Hoş Geldiniz',
      originalFollowMessage: 'En son haberler ve duyurular için sitemizi takip edin.',
      intervalId: null, // Interval ID'yi saklamak için
      typingSpeed: 50, // Karakter ekleme hızı (milisaniye)
    };
  },
  mounted() {
    this.animateText();
  },
  beforeDestroy() {
    // Bileşen yok edilirken interval'i temizleyin
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  },
  methods: {
    animateText() {
      let welcomeIndex = 0;
      let followIndex = 0;
      const welcomeLength = this.originalWelcomeMessage.length;
      const followLength = this.originalFollowMessage.length;

      this.intervalId = setInterval(() => {
        if (welcomeIndex < welcomeLength) {
          this.welcomeMessage += this.originalWelcomeMessage[welcomeIndex];
          welcomeIndex++;
          console.log('Welcome Message:', this.welcomeMessage);
        } else if (followIndex < followLength) {
          this.followMessage += this.originalFollowMessage[followIndex];
          followIndex++;
          console.log('Follow Message:', this.followMessage);
        } else {
          clearInterval(this.intervalId);
          this.intervalId = null;
        }
      }, this.typingSpeed); // Hızı data üzerinden kontrol ediyoruz
    },
  },
};
</script>

<style scoped>
.home {
  height: calc(100vh - 60px); /* Navbar yüksekliğini çıkarın */
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.parallax {
  background-image: url('https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0');
  background-size: cover;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  animation: parallaxScroll 20s linear infinite;
}

@keyframes parallaxScroll {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-50px);
  }
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(44, 62, 80, 0.6);
  z-index: 1;
}

.content {
  position: relative;
  z-index: 2;
  background-color: rgba(255, 255, 255, 0.85);
  padding: 60px 40px;
  border-radius: 15px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  max-width: 700px;
  text-align: center;
}

h1 {
  font-size: 3em;
  font-weight: 700;
  margin-bottom: 20px;
  color: #2ecc71;
  font-family: 'Roboto', sans-serif;
  animation: fadeIn 1.5s ease-in-out forwards;
  opacity: 0; /* Başlangıçta gizli */
}

p {
  font-size: 1.3em;
  margin-bottom: 30px;
  color: #34495e;
  font-family: 'Open Sans', sans-serif;
  animation: fadeIn 2s ease-in-out forwards;
  opacity: 0; /* Başlangıçta gizli */
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
}

.btn {
  padding: 15px 30px;
  font-size: 1.2em;
  color: #ffffff;
  background-color: #e74c3c;
  border: none;
  border-radius: 25px;
  text-decoration: none;
  transition: background-color 0.3s, transform 0.3s;
  box-shadow: 0 5px 15px rgba(231, 76, 60, 0.3);
}

.btn:hover {
  background-color: #c0392b;
  transform: scale(1.05);
}

@keyframes fadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive */
@media (max-width: 768px) {
  .content {
    padding: 40px 20px;
  }

  h1 {
    font-size: 2.2em;
  }

  p {
    font-size: 1.1em;
  }

  .btn {
    font-size: 1em;
    padding: 12px 24px;
  }
}
</style>
