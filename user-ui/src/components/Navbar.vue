<!-- src/components/Navbar.vue -->
<template>
  <nav class="navbar">
    <div class="logo">
      <router-link to="/">CMV Bilişim</router-link>
    </div>
    <ul :class="['nav-links', { open: isOpen }]">
      <li>
        <router-link to="/">Anasayfa</router-link>
      </li>
      <li>
        <router-link to="/announcements">Duyurular</router-link>
      </li>
      <li>
        <router-link to="/news">Haberler</router-link>
      </li>
    </ul>
    <div class="hamburger" @click="toggleMenu">
      <span :class="{ 'change': isOpen }"></span>
      <span :class="{ 'change': isOpen }"></span>
      <span :class="{ 'change': isOpen }"></span>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar',
  data() {
    return {
      isOpen: false,
    };
  },
  methods: {
    toggleMenu() {
      this.isOpen = !this.isOpen;
    },
  },
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2c3e50;
  padding: 15px 30px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.logo a {
  color: #ecf0f1;
  font-size: 1.8em;
  font-weight: bold;
  text-decoration: none;
  font-family: 'Roboto', sans-serif;
}

.nav-links {
  list-style: none;
  display: flex;
  gap: 20px;
  transition: transform 0.5s ease-in;
}

.nav-links.open {
  transform: translateX(0);
}

.nav-links li a {
  color: #ecf0f1;
  text-decoration: none;
  font-size: 1.1em;
  position: relative;
  transition: color 0.3s;
}

.nav-links li a::after {
  content: '';
  display: block;
  width: 0;
  height: 2px;
  background: #e74c3c;
  transition: width 0.3s;
}

.nav-links li a:hover {
  color: #e74c3c;
}

.nav-links li a:hover::after {
  width: 100%;
}

.hamburger {
  display: none;
  flex-direction: column;
  cursor: pointer;
}

.hamburger span {
  height: 3px;
  width: 25px;
  background: #ecf0f1;
  margin: 4px 0;
  transition: 0.4s;
}

/* Hamburger Animation */
.hamburger .change:nth-child(1) {
  transform: rotate(-45deg) translate(-5px, 6px);
}

.hamburger .change:nth-child(2) {
  opacity: 0;
}

.hamburger .change:nth-child(3) {
  transform: rotate(45deg) translate(-5px, -6px);
}

/* Responsive */
@media (max-width: 768px) {
  .nav-links {
    position: absolute;
    top: 60px;
    right: 0;
    height: calc(100vh - 60px);
    width: 100%;
    background-color: #2c3e50;
    flex-direction: column;
    align-items: center;
    justify-content: start;
    gap: 30px;
    transform: translateX(100%);
  }

  .hamburger {
    display: flex;
  }

  .hamburger .change {
    background: #ecf0f1;
  }
}
</style>
