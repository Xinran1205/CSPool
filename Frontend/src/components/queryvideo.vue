<template>
  <h1>CSPool</h1>

<form name="myForm" id="form">
  Query Video: <input type="text" id="videoname" name="fname">
  <el-button color="#626aef" :dark="isDark" type="primary" :icon="Search" @click="Getdata">Search</el-button>
</form>

<p id=x>
</p >
</template>

<script >
import axios from 'axios'
export default {
  name: 'queryvideo',
  data:function(){
    return {
      videos123:[]
    }
  }, 

  methods: {
    Getdata(){
      var a = document.getElementById("videoname").value;
      axios.get("http://localhost:8081/video/"+a).then(function(response){
      document.getElementById("x").innerHTML = "课程名：" + response.data.data.name;
    }).catch((e) => {
      alert("你是遭搞者")
      this.errors.push(e);
    });
    }
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
