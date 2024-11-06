// 保存提交的配置信息到本地存储
function saveSubmitHistory(config) {
  const history = JSON.parse(localStorage.getItem('submitHistory')) || [];
  history.unshift(config);
  localStorage.setItem('submitHistory', JSON.stringify(history));
}

// 提交配置信息
function submitConfig() {
  const config = {
    sdkUrl: document.getElementById('sdkUrl').value,
    apkPackageName: document.getElementById('apkPackageName').value,
    sdkPackageName: document.getElementById('sdkPackageName').value,
    appId: document.getElementById('appId').value
  };
  saveSubmitHistory(config);
  console.log('提交的配置信息：', config);
  goToHistory(); // 提交后跳转到历史记录页面
}

// 显示历史记录
function displayHistory() {
  const history = JSON.parse(localStorage.getItem('submitHistory')) || [];
  const historyList = document.getElementById('historyList');
  historyList.innerHTML = ''; // 清空历史列表
  history.forEach((item, index) => {
    const li = document.createElement('li');
    li.textContent = `历史记录 ${index + 1}: ${JSON.stringify(item)}`;
    historyList.appendChild(li);
  });
}

// 跳转到配置页面
function goToConfig() {
  window.location.href = './index.html';
}

// 跳转到历史记录页面
function goToHistory() {
  window.location.href = './history.html';
}

// 在历史记录页面加载时显示历史记录
document.addEventListener('DOMContentLoaded', function() {
  if (window.location.pathname.endsWith('history.html')) {
    displayHistory();
  }
});
