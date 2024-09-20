<template>
  <div class="home">
    <div class="parallax">
      <div class="content">
        <h1 class="fade-in">Derneğimize Hoş Geldiniz</h1>
        <p class="fade-in">En son haberler ve duyurular için sitemizi takip edin.</p>
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
  name: 'Home'
};
</script>

<style scoped>
html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}

.home {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  margin: 0;
  padding: 0;
  overflow: hidden;
  width: 100%;
}

.parallax {
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://example.com/your-image.jpg') no-repeat center center;
  background-size: cover;
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  margin: 0;
  padding: 0;
}

.content {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 50px;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
  z-index: 1;
  max-width: 600px;
}

h1 {
  font-size: 3.2em;
  font-weight: bold;
  margin-bottom: 20px;
  color: #2ecc71;
  animation: fadeIn 2s ease-in;
}

p {
  font-size: 1.4em;
  margin-bottom: 30px;
  color: #34495e;
  animation: fadeIn 3s ease-in;
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn {
  padding: 12px 24px;
  font-size: 1.2em;
  color: white;
  background-color: #e74c3c;
  border-radius: 8px;
  text-decoration: none;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #c0392b;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@media (max-width: 768px) {
  h1 {
    font-size: 2.4em;
  }

  p {
    font-size: 1.2em;
  }

  .btn {
    font-size: 1em;
    padding: 10px 20px;
  }
}
</style>