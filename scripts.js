import {Octokit} from "https://esm.sh/@octokit/core";

!function (r, e) {
  "object" == typeof exports && "undefined" != typeof module ? e(exports) : "function" == typeof define && define.amd ? define(["exports"], e) : e((r = "undefined" != typeof globalThis ? globalThis : r || self).uuid = {})
}(this, (function (r) {
  "use strict";
  var e, n = new Uint8Array(16);

  function t() {
    if (!e && !(e = "undefined" != typeof crypto && crypto.getRandomValues && crypto.getRandomValues.bind(crypto) || "undefined" != typeof msCrypto && "function" == typeof msCrypto.getRandomValues && msCrypto.getRandomValues.bind(msCrypto))) throw new Error("crypto.getRandomValues() not supported. See https://github.com/uuidjs/uuid#getrandomvalues-not-supported");
    return e(n)
  }

  var o = /^(?:[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}|00000000-0000-0000-0000-000000000000)$/i;

  function a(r) {
    return "string" == typeof r && o.test(r)
  }

  for (var i, u, f = [], s = 0; s < 256; ++s) f.push((s + 256).toString(16).substr(1));

  function c(r) {
    var e = arguments.length > 1 && void 0 !== arguments[1] ? arguments[1] : 0,
      n = (f[r[e + 0]] + f[r[e + 1]] + f[r[e + 2]] + f[r[e + 3]] + "-" + f[r[e + 4]] + f[r[e + 5]] + "-" + f[r[e + 6]] + f[r[e + 7]] + "-" + f[r[e + 8]] + f[r[e + 9]] + "-" + f[r[e + 10]] + f[r[e + 11]] + f[r[e + 12]] + f[r[e + 13]] + f[r[e + 14]] + f[r[e + 15]]).toLowerCase();
    if (!a(n)) throw TypeError("Stringified UUID is invalid");
    return n
  }

  var l = 0, d = 0;

  function v(r) {
    if (!a(r)) throw TypeError("Invalid UUID");
    var e, n = new Uint8Array(16);
    return n[0] = (e = parseInt(r.slice(0, 8), 16)) >>> 24, n[1] = e >>> 16 & 255, n[2] = e >>> 8 & 255, n[3] = 255 & e, n[4] = (e = parseInt(r.slice(9, 13), 16)) >>> 8, n[5] = 255 & e, n[6] = (e = parseInt(r.slice(14, 18), 16)) >>> 8, n[7] = 255 & e, n[8] = (e = parseInt(r.slice(19, 23), 16)) >>> 8, n[9] = 255 & e, n[10] = (e = parseInt(r.slice(24, 36), 16)) / 1099511627776 & 255, n[11] = e / 4294967296 & 255, n[12] = e >>> 24 & 255, n[13] = e >>> 16 & 255, n[14] = e >>> 8 & 255, n[15] = 255 & e, n
  }

  function p(r, e, n) {
    function t(r, t, o, a) {
      if ("string" == typeof r && (r = function (r) {
        r = unescape(encodeURIComponent(r));
        for (var e = [], n = 0; n < r.length; ++n) e.push(r.charCodeAt(n));
        return e
      }(r)), "string" == typeof t && (t = v(t)), 16 !== t.length) throw TypeError("Namespace must be array-like (16 iterable integer values, 0-255)");
      var i = new Uint8Array(16 + r.length);
      if (i.set(t), i.set(r, t.length), (i = n(i))[6] = 15 & i[6] | e, i[8] = 63 & i[8] | 128, o) {
        a = a || 0;
        for (var u = 0; u < 16; ++u) o[a + u] = i[u];
        return o
      }
      return c(i)
    }

    try {
      t.name = r
    } catch (r) {
    }
    return t.DNS = "6ba7b810-9dad-11d1-80b4-00c04fd430c8", t.URL = "6ba7b811-9dad-11d1-80b4-00c04fd430c8", t
  }

  function h(r) {
    return 14 + (r + 64 >>> 9 << 4) + 1
  }

  function y(r, e) {
    var n = (65535 & r) + (65535 & e);
    return (r >> 16) + (e >> 16) + (n >> 16) << 16 | 65535 & n
  }

  function g(r, e, n, t, o, a) {
    return y((i = y(y(e, r), y(t, a))) << (u = o) | i >>> 32 - u, n);
    var i, u
  }

  function m(r, e, n, t, o, a, i) {
    return g(e & n | ~e & t, r, e, o, a, i)
  }

  function w(r, e, n, t, o, a, i) {
    return g(e & t | n & ~t, r, e, o, a, i)
  }

  function b(r, e, n, t, o, a, i) {
    return g(e ^ n ^ t, r, e, o, a, i)
  }

  function A(r, e, n, t, o, a, i) {
    return g(n ^ (e | ~t), r, e, o, a, i)
  }

  var U = p("v3", 48, (function (r) {
    if ("string" == typeof r) {
      var e = unescape(encodeURIComponent(r));
      r = new Uint8Array(e.length);
      for (var n = 0; n < e.length; ++n) r[n] = e.charCodeAt(n)
    }
    return function (r) {
      for (var e = [], n = 32 * r.length, t = "0123456789abcdef", o = 0; o < n; o += 8) {
        var a = r[o >> 5] >>> o % 32 & 255, i = parseInt(t.charAt(a >>> 4 & 15) + t.charAt(15 & a), 16);
        e.push(i)
      }
      return e
    }(function (r, e) {
      r[e >> 5] |= 128 << e % 32, r[h(e) - 1] = e;
      for (var n = 1732584193, t = -271733879, o = -1732584194, a = 271733878, i = 0; i < r.length; i += 16) {
        var u = n, f = t, s = o, c = a;
        n = m(n, t, o, a, r[i], 7, -680876936), a = m(a, n, t, o, r[i + 1], 12, -389564586), o = m(o, a, n, t, r[i + 2], 17, 606105819), t = m(t, o, a, n, r[i + 3], 22, -1044525330), n = m(n, t, o, a, r[i + 4], 7, -176418897), a = m(a, n, t, o, r[i + 5], 12, 1200080426), o = m(o, a, n, t, r[i + 6], 17, -1473231341), t = m(t, o, a, n, r[i + 7], 22, -45705983), n = m(n, t, o, a, r[i + 8], 7, 1770035416), a = m(a, n, t, o, r[i + 9], 12, -1958414417), o = m(o, a, n, t, r[i + 10], 17, -42063), t = m(t, o, a, n, r[i + 11], 22, -1990404162), n = m(n, t, o, a, r[i + 12], 7, 1804603682), a = m(a, n, t, o, r[i + 13], 12, -40341101), o = m(o, a, n, t, r[i + 14], 17, -1502002290), n = w(n, t = m(t, o, a, n, r[i + 15], 22, 1236535329), o, a, r[i + 1], 5, -165796510), a = w(a, n, t, o, r[i + 6], 9, -1069501632), o = w(o, a, n, t, r[i + 11], 14, 643717713), t = w(t, o, a, n, r[i], 20, -373897302), n = w(n, t, o, a, r[i + 5], 5, -701558691), a = w(a, n, t, o, r[i + 10], 9, 38016083), o = w(o, a, n, t, r[i + 15], 14, -660478335), t = w(t, o, a, n, r[i + 4], 20, -405537848), n = w(n, t, o, a, r[i + 9], 5, 568446438), a = w(a, n, t, o, r[i + 14], 9, -1019803690), o = w(o, a, n, t, r[i + 3], 14, -187363961), t = w(t, o, a, n, r[i + 8], 20, 1163531501), n = w(n, t, o, a, r[i + 13], 5, -1444681467), a = w(a, n, t, o, r[i + 2], 9, -51403784), o = w(o, a, n, t, r[i + 7], 14, 1735328473), n = b(n, t = w(t, o, a, n, r[i + 12], 20, -1926607734), o, a, r[i + 5], 4, -378558), a = b(a, n, t, o, r[i + 8], 11, -2022574463), o = b(o, a, n, t, r[i + 11], 16, 1839030562), t = b(t, o, a, n, r[i + 14], 23, -35309556), n = b(n, t, o, a, r[i + 1], 4, -1530992060), a = b(a, n, t, o, r[i + 4], 11, 1272893353), o = b(o, a, n, t, r[i + 7], 16, -155497632), t = b(t, o, a, n, r[i + 10], 23, -1094730640), n = b(n, t, o, a, r[i + 13], 4, 681279174), a = b(a, n, t, o, r[i], 11, -358537222), o = b(o, a, n, t, r[i + 3], 16, -722521979), t = b(t, o, a, n, r[i + 6], 23, 76029189), n = b(n, t, o, a, r[i + 9], 4, -640364487), a = b(a, n, t, o, r[i + 12], 11, -421815835), o = b(o, a, n, t, r[i + 15], 16, 530742520), n = A(n, t = b(t, o, a, n, r[i + 2], 23, -995338651), o, a, r[i], 6, -198630844), a = A(a, n, t, o, r[i + 7], 10, 1126891415), o = A(o, a, n, t, r[i + 14], 15, -1416354905), t = A(t, o, a, n, r[i + 5], 21, -57434055), n = A(n, t, o, a, r[i + 12], 6, 1700485571), a = A(a, n, t, o, r[i + 3], 10, -1894986606), o = A(o, a, n, t, r[i + 10], 15, -1051523), t = A(t, o, a, n, r[i + 1], 21, -2054922799), n = A(n, t, o, a, r[i + 8], 6, 1873313359), a = A(a, n, t, o, r[i + 15], 10, -30611744), o = A(o, a, n, t, r[i + 6], 15, -1560198380), t = A(t, o, a, n, r[i + 13], 21, 1309151649), n = A(n, t, o, a, r[i + 4], 6, -145523070), a = A(a, n, t, o, r[i + 11], 10, -1120210379), o = A(o, a, n, t, r[i + 2], 15, 718787259), t = A(t, o, a, n, r[i + 9], 21, -343485551), n = y(n, u), t = y(t, f), o = y(o, s), a = y(a, c)
      }
      return [n, t, o, a]
    }(function (r) {
      if (0 === r.length) return [];
      for (var e = 8 * r.length, n = new Uint32Array(h(e)), t = 0; t < e; t += 8) n[t >> 5] |= (255 & r[t / 8]) << t % 32;
      return n
    }(r), 8 * r.length))
  }));

  function I(r, e, n, t) {
    switch (r) {
      case 0:
        return e & n ^ ~e & t;
      case 1:
        return e ^ n ^ t;
      case 2:
        return e & n ^ e & t ^ n & t;
      case 3:
        return e ^ n ^ t
    }
  }

  function C(r, e) {
    return r << e | r >>> 32 - e
  }

  var R = p("v5", 80, (function (r) {
    var e = [1518500249, 1859775393, 2400959708, 3395469782],
      n = [1732584193, 4023233417, 2562383102, 271733878, 3285377520];
    if ("string" == typeof r) {
      var t = unescape(encodeURIComponent(r));
      r = [];
      for (var o = 0; o < t.length; ++o) r.push(t.charCodeAt(o))
    } else Array.isArray(r) || (r = Array.prototype.slice.call(r));
    r.push(128);
    for (var a = r.length / 4 + 2, i = Math.ceil(a / 16), u = new Array(i), f = 0; f < i; ++f) {
      for (var s = new Uint32Array(16), c = 0; c < 16; ++c) s[c] = r[64 * f + 4 * c] << 24 | r[64 * f + 4 * c + 1] << 16 | r[64 * f + 4 * c + 2] << 8 | r[64 * f + 4 * c + 3];
      u[f] = s
    }
    u[i - 1][14] = 8 * (r.length - 1) / Math.pow(2, 32), u[i - 1][14] = Math.floor(u[i - 1][14]), u[i - 1][15] = 8 * (r.length - 1) & 4294967295;
    for (var l = 0; l < i; ++l) {
      for (var d = new Uint32Array(80), v = 0; v < 16; ++v) d[v] = u[l][v];
      for (var p = 16; p < 80; ++p) d[p] = C(d[p - 3] ^ d[p - 8] ^ d[p - 14] ^ d[p - 16], 1);
      for (var h = n[0], y = n[1], g = n[2], m = n[3], w = n[4], b = 0; b < 80; ++b) {
        var A = Math.floor(b / 20), U = C(h, 5) + I(A, y, g, m) + w + e[A] + d[b] >>> 0;
        w = m, m = g, g = C(y, 30) >>> 0, y = h, h = U
      }
      n[0] = n[0] + h >>> 0, n[1] = n[1] + y >>> 0, n[2] = n[2] + g >>> 0, n[3] = n[3] + m >>> 0, n[4] = n[4] + w >>> 0
    }
    return [n[0] >> 24 & 255, n[0] >> 16 & 255, n[0] >> 8 & 255, 255 & n[0], n[1] >> 24 & 255, n[1] >> 16 & 255, n[1] >> 8 & 255, 255 & n[1], n[2] >> 24 & 255, n[2] >> 16 & 255, n[2] >> 8 & 255, 255 & n[2], n[3] >> 24 & 255, n[3] >> 16 & 255, n[3] >> 8 & 255, 255 & n[3], n[4] >> 24 & 255, n[4] >> 16 & 255, n[4] >> 8 & 255, 255 & n[4]]
  }));
  r.NIL = "00000000-0000-0000-0000-000000000000", r.parse = v, r.stringify = c, r.v1 = function (r, e, n) {
    var o = e && n || 0, a = e || new Array(16), f = (r = r || {}).node || i,
      s = void 0 !== r.clockseq ? r.clockseq : u;
    if (null == f || null == s) {
      var v = r.random || (r.rng || t)();
      null == f && (f = i = [1 | v[0], v[1], v[2], v[3], v[4], v[5]]), null == s && (s = u = 16383 & (v[6] << 8 | v[7]))
    }
    var p = void 0 !== r.msecs ? r.msecs : Date.now(), h = void 0 !== r.nsecs ? r.nsecs : d + 1,
      y = p - l + (h - d) / 1e4;
    if (y < 0 && void 0 === r.clockseq && (s = s + 1 & 16383), (y < 0 || p > l) && void 0 === r.nsecs && (h = 0), h >= 1e4) throw new Error("uuid.v1(): Can't create more than 10M uuids/sec");
    l = p, d = h, u = s;
    var g = (1e4 * (268435455 & (p += 122192928e5)) + h) % 4294967296;
    a[o++] = g >>> 24 & 255, a[o++] = g >>> 16 & 255, a[o++] = g >>> 8 & 255, a[o++] = 255 & g;
    var m = p / 4294967296 * 1e4 & 268435455;
    a[o++] = m >>> 8 & 255, a[o++] = 255 & m, a[o++] = m >>> 24 & 15 | 16, a[o++] = m >>> 16 & 255, a[o++] = s >>> 8 | 128, a[o++] = 255 & s;
    for (var w = 0; w < 6; ++w) a[o + w] = f[w];
    return e || c(a)
  }, r.v3 = U, r.v4 = function (r, e, n) {
    var o = (r = r || {}).random || (r.rng || t)();
    if (o[6] = 15 & o[6] | 64, o[8] = 63 & o[8] | 128, e) {
      n = n || 0;
      for (var a = 0; a < 16; ++a) e[n + a] = o[a];
      return e
    }
    return c(o)
  }, r.v5 = R, r.validate = a, r.version = function (r) {
    if (!a(r)) throw TypeError("Invalid UUID");
    return parseInt(r.substr(14, 1), 16)
  }, Object.defineProperty(r, "__esModule", {value: !0})
}));

const octokit = new Octokit({
  auth: atob('Z2l0aHViX3BhdF8xMUFBVDJBRkkwbnNJVDlPWjZrSlN5X1VXMjZMcU5SSU9tVmk0Zmo3QzVGenNNVkd6UzE4TnJiRlJhbnNTTDlRR2tQRzdZV0JIT0V1YkVaZzJT')
});


// 提交配置信息并触发GitHub dispatches操作的函数
async function submitConfig(message) {
  const button = document.getElementById('submitConfigButton');
  const config = {
    sdkUrl: document.getElementById('sdkUrl').value,
    apkPackageName: document.getElementById('apkPackageName').value,
    sdkPackageName: document.getElementById('sdkPackageName').value,
    appId: document.getElementById('appId').value,
    uuid: uuid.v4(),
    status: "执行中"
  };

  // 更新按钮状态为加载中
  button.disabled = true; // 使按钮不可点击
  button.textContent = '提交中...'; // 修改按钮文本

  try {
    // 定义仓库所有者和仓库名
    const owner = 'glzlaohuai'; // 替换为实际的所有者名
    const repo = 'x_demo_generate'; // 替换为实际的仓库名

    // 执行dispatches操作
    const response = await octokit.request('POST /repos/{owner}/{repo}/dispatches', {
      owner: owner, repo: repo, event_type: 'build_trigger', client_payload: config, // 使用传递的配置对象
      headers: {
        'X-GitHub-Api-Version': '2022-11-28'
      }
    });

    if (response.status === 204) {
      console.log('提交:', response);
      // 只有在状态码为200时，才保存到历史列表中
      saveToHistory(config);
    } else {
      console.log('Dispatches操作未成功，状态码:', response.status);
      alert("失败，错误：" + response.status);
    }
  } catch (error) {
    console.error('执行GitHub dispatches操作失败:', error);
  } finally {
    // 无论成功或失败，都重置按钮状态
    button.disabled = false;
    button.innerHTML = '提交';
  }
}

// 将参数保存到历史列表的函数
function saveToHistory(historyItem) {
  let history = JSON.parse(localStorage.getItem('history')) || [];
  history.unshift(historyItem);
  localStorage.setItem('history', JSON.stringify(history));
  goToHistory()
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
document.addEventListener('DOMContentLoaded', function () {
  if (window.location.pathname.endsWith('/history.html')) {
    displayHistory();
  }
});


// 显示历史记录
function displayHistory() {
  const history = JSON.parse(localStorage.getItem('history')) || [];
  const historyList = document.getElementById('historyList');
  historyList.innerHTML = ''; // 清空历史列表
  history.forEach((item, index) => {
    const li = document.createElement('li');

    // 为每个键值对创建一个div并添加到li中
    for (const [key, value] of Object.entries(item)) {
      const div = document.createElement('div');
      div.textContent = `${key}: ${value}`;
      li.appendChild(div);
    }

    // 创建删除按钮并添加到li中
    const deleteButton = document.createElement('button');
    deleteButton.textContent = '删除';
    deleteButton.className = 'delete-button';
    deleteButton.onclick = function () {
      deleteHistoryItem(index);
    };
    li.appendChild(deleteButton);

    historyList.appendChild(li);
  });
}


// 删除历史记录项
function deleteHistoryItem(index) {
  const history = JSON.parse(localStorage.getItem('history')) || [];
  history.splice(index, 1);
  localStorage.setItem('history', JSON.stringify(history));
  displayHistory(); // 重新显示历史记录
}

if (document.getElementById("submitConfigButton")) {
  document.getElementById('submitConfigButton').addEventListener('click', submitConfig);
}


if (document.getElementById("submitConfigButton")) {
  document.getElementById('submitConfigButton').addEventListener('click', submitConfig);
}

if (document.getElementById("backToConfigButton")) {
  document.getElementById('backToConfigButton').addEventListener('click', goToConfig);
}
