<template>
  <img alt="cspool logo" class="image1" src="./assets/cspool.png">
  <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
  <queryvideo></queryvideo>
</template>

<script>
import { assertExpressionStatement } from '@babel/types';
import queryvideo from './components/queryvideo.vue'

export default {
  name: 'App',
  components: {
    queryvideo
  },
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.image1{
  width: 300px;
  height: 300px;
}
</style>
