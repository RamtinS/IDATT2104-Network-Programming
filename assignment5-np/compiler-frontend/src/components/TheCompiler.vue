<script setup>

import {ref} from "vue";
import axios from "axios";

const userInput = ref("");
const userOutput = ref("");

/**
 * Function to run the code.
 *
 * @function runCode
 * @async
 * @returns {Promise<void>} - A promise that resolves when the code is executed.
 */
async function runCode() {

  const sendData = {
    code: userInput.value
  };

  try {
    const response = await axios.post("http://localhost:8080/compiler/cpp", sendData);
    userOutput.value = response.data.responseCode;
    console.log(response.data.responseCode);
  } catch (error) {
    console.error("Error");
  }
}

</script>

<template>
  <div id="compiler">
    <h1>Online C++ compiler</h1>
    <div id="flex-container">
      <div class="column">
        <textarea id="userInput" v-model="userInput" placeholder="Enter your code!"></textarea>
      </div>
      <div class="column">
        <textarea :value="userOutput" readonly></textarea>
      </div>
    </div>
    <button id="run-button" :disabled="userInput === '' " v-on:click="runCode">Run code</button>
  </div>
</template>

<style scoped>

h1 {
  font-weight: bold;
  text-align: center;
}

#flex-container {
  display: flex;
  margin-top: 20px;
  margin-bottom: 20px;
}

.column {
  flex: 1;
  padding: 10px;
  border: 1px solid #cccc;
}

textarea {
  min-width: 100%;
  width: 100%;
  height: 550px;
  background-color: white;
  resize: none;
}

#run-button {
  margin-left: 20%;
  background-color: dodgerblue;
  color: white;
  padding: 6px 10px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 100%;
  font-weight: bold;
  transition: opacity 0.15s;
}

#run-button[disabled] {
  background-color: #ccc;
  color: #666;
  cursor: not-allowed;
}

#run-button:hover {
  opacity: 0.8;
  box-shadow: 5px 5px 5px gray;
}

#run-button:active {
  background-color: green;
}

#run-button:active[disabled] {
  background-color: #ccc;
}

</style>