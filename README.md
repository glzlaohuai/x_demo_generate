# AppicAd Android SDK接入文档 [（English version）](#English)

* [基础SDK接入](#essential)
* [接入原生广告](#nativeAD)
* [接入开屏广告](#splashAD)
* [接入插屏广告](#interstitialAD)
* [接入横幅广告](#bannerAD)
* [接入激励视频广告](#rewardedVideoAd)
* [其他](#others)


# SDK接入
* 接入工程的`app module`的`build.gradle`中添加依赖：
```java
implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
```

* `applicaton `（推荐）或入口activity 的`onCreate`回调方法中执行初始化
```
APSDK.init(context, "appID",listener);
```
**APSDK初始化必须在主进程进行，需要在所有广告请求之前，请在APSDK回调成功后再请求广告**

**注**:`appID`的值将在您接入sdk时由我方相关对接人员提供。


* `proguard`配置：

```
	-keep public class com.google.android.gms.**
	-dontwarn com.google.android.gms.**
	-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
	 }
	 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
	 }
	 
	-keep class com.adup.** {*;}
	-keep class com.apd.** { *; }
	-dontwarn com.adup.**
```

* 资源混淆配置
在 `res/raw/`目录下新增 `keep.xml`文件把如下内容添加到文件内：

```
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools"
    tools:keep="@layout/ap_*,@anim/ap_*,@drawable/ap_*,@string/ap_*,@style/ap_*,@dimen/ap_*,@color/ap_*,@bool/ap_*,@raw/apsdk_*,@xml/ap_*"
    />
```

如果您的工程中接入了资源混淆插件**AabResGuard**，为了保证SDK的资源可以被正常使用，需要新增白名单配置，内容如下：

```
"*.R.id.ap_*",
"*.R.anim.ap_*",
"*.R.drawable.ap_*",
"*.R.layout.ap_*",
"*.R.string.ap_*",
"*.R.dimen.ap_*",
"*.R.color.ap_*",
"*.R.bool.ap_*",
"*.R.style.ap_*",
"*/res/anim/ap_*",
"*/res/drawable/ap_*",
"*/res/drawable-xxhdpi-v4/ap_*",
"*/res/layout/ap_*",
"*/res/raw/apsdk_*",
"*/res/xml/ap_*"
```

* 更新备注
APAdError 这个类的路径在5.0.5版本有调整，若从之前版本升级，需要修改一下引用：com.adup.sdk.core.others.APAdError


#### 获取SDK版本号
```java
public String sdkVersion();
```

#### APSDKListener
```java
// SDK初始化成功
public void onSDKInitializeSuccess();

// SDK初始化失败
public void onSDKInitializeFail(APAdError err);
```


# <a id="nativeAD">接入原生广告 - Native</a>
## 创建原生广告实例
```
APAdNative apNative = new APAdNative("slotID", listener);
```
| 参数	|	说明 |
| ---	|	--- |
| activity	|	创建该实例所处的activity |
| slotID	|	广告位id |
| listener	|	原生广告加载结果回调 |

## 加载广告

```java
apNative.load();
```
## 加载成功后（收到加载成功的回调），从APNative实例中获取广告相关内容

| 说明	        | 	方法                       | 备注 |
|------------|---------------------------| --- |
| icon	      | 	`getAPAdIcon()`          | |
| icon url	  | 	`getAPAdIconUrl()`       | |
| image	     | 	`getAPAdScreenshot()`    | |
| image url  | 	`getAPAdScreenshotUrl()` | |
| 描述文字	      | 	`getAPAdDescription()`   | |
| 标题文字	      | 	`getAPAdTitle()`         ||
| 广告视频	      | 	`getAPAdVideo()`         |可以通过此方法的返回值判断广告是否为`视频类型`，非视频返回`null`|

## 设置Binder
设置binder来告知sdk各个原生内容被设置在了哪个组件上，需要在执行`bindAdToView`方法前调用。

```
public void setViewBinder(APNativeViewBinder binder)
```

`binder`参数构造示例：
```
APNativeViewBinder.Builder.create().setTitleViewID(titleViewID).setDescViewID(descViewID).setIconViewID(iconViewID).setScreenshotViewID(screenshotViewID).build();
```




## View与广告绑定
View和广告的绑定，必须设置APAdNativeAdContainer

clickableViews为触发广告点击行为的View，必须在container中，不然不会响应点击事件.

不能对clickableViews设置OnClickListener，会影响点击事件的上报，点击事件回调可通过`onApAdNativeDidClick`回调监听

```java
public void bindAdToView(APAdNativeAdContainer container, List<View> clickableViews)
```

## 接口-原生状态管理
当宿主Activity.onResume()时调用，恢复广告状态，如果不调用会导致广告状态错乱
```java
public void onResume();
```

## 设置Deeplink弹窗
设置Deeplink弹窗 - 如果不调用此方法设置，则点击广告不弹窗，直接跳转目标应用。
```java
public void setDeeplinkTipWithTitle(String title);
```

## Listener

**`APAdNativeListner`**

```java
// 当广告成功填充，并加载完成后触发此回调
// @param ad 加载成功的原生广告
public void onApAdNativeDidLoadSuccess(APAdNative ad);

// 当广告填充或者加载失败后触发此回调
// @param ad 加载成功的原生广告
// @param err 加载失败原因
public void onApAdNativeDidLoadFail(APAdNative ad, APAdError err);

// 当广告成功展示后触发此回调
// @param ad 展示成功的原生广告
public void onAPAdNativePresentSuccess(APAdNative ad);

// 当广告被点击后触发此回调
// @param ad 被点击的原生广告
public void onApAdNativeDidClick(APAdNative ad);

// 当广点击后完成展示落地页
// @param ad 展示成功的原生广告
public void onApAdNativeDidPresentLanding(APAdNative ad);

// 当广告加载完毕落地页后关闭落地页
// @param ad 展示失败的原生广告
public void onApAdNativeDidDismissLanding(APAdNative ad);

// 当广告点击后将跳转出应用
// @param ad 展示成功的原生广告
public void onApAdNativeApplicationWillEnterBackground(APAdNative ad);
```
**`APAdNativeVideoView`**
```java
// 当视频播放完毕
// @param view 视频素材元件播放视图
public void onApAdNativeVideoViewDidPlayFinish(APAdNativeVideoView view);
```

## APAdNativeVideoView - 接口
原生广告视频素材元件对接接口

| 接口	|	说明 | 备注 |
| ---	|	--- | --- |
| `public void setMute(boolean mute);`	|	设置播放器静音 |mute YES：开启静音，NO：关闭静音 |
| `public void play() ;`	|	开始播放视频 | |
| `public void pause();`	|	暂停播放视频 | |


**`APAdNativeVideoState`**
| 名称	|	备注 |
| ---	|	--- |
| `APAdNativeVideoStateDefault`	|	视频初始化默认状态 |
| `APAdNativeVideoStateFailed`	| 播放失败 |
| `APAdNativeVideoStateBuffering`	|	视频缓冲中 |
| `APAdNativeVideoStatePlaying`	|	视频播放中 |
| `APAdNativeVideoStateStop`	|	视频播放停止 |
| `APAdNativeVideoStatePause`	|	视频播放暂停 |



# <a id="splashAD">接入开屏广告 - APAdSplash</a>

## 创建开屏广告实例：

```java
APAdSplash splash = new APAdSplash(slotID,listener);
```

| 参数	|	说明 |
| --- | --- |
| slotID	|	广告位id |
| listener	|	开屏广告加载、展示、关闭等的结果回调 |

## 加载广告
调用下面方法加载广告

```java
public void load();
```

## 展示广告
调用下面方法展示广告

```java
public void presentWithViewContainer(ViewGroup container);
```
* **ViewGroup** - 用于展示开屏广告

## 加载 & 展示广告
调用下面方法加载并展示开屏广告 `APAdSplash`

```java
public void loadAndPresentWithViewContainer(ViewGroup container);
```
* **ViewGroup** - 用于展示开屏广告
**注**:`loadAndPresent`与`load`不可同时使用。

## 设置加载时间
调用下面方法设置广告加载时间，时长必须>=0，默认为3秒

```java
public boolean setSplashMaxLoadInterval(double interval); //单位秒
```

## 设置广告显示时长
调用下面方法设置广告显示时长

```java
public boolean setSplashShowInterval(int interval); //单位秒
```

## 设置背景颜色
调用下面方法设置广告背景颜色，默认：黑色。
```java
public void setSplashBackgroundColor(int color);
```

## 设置背景图片
调用下面方法设置广告背景图片，默认：不显示。
```java
public void setSplashBackgroundColor(Bitmap image);
```
**注** :`背景颜色`和`背景图片`同时使用时，背景图片会覆盖背景颜色

## 设置广告关闭按钮视图
调用下面方法自定义指定关闭按钮样式
```java
public void setSplashCloseButtonView(View view);
```

## 设置广告关闭按钮位置
调用下面方法自定义指定关闭按钮的位置
```java
public void setSplashCloseButtonPosition(ViewGroup.LayoutParams params);
```

## 设置Deeplink弹窗
设置Deeplink弹窗 - 如果不调用此方法设置，则点击广告不弹窗，直接跳转目标应用。
```java
public void setDeeplinkTipWithTitle(String title);
```

## 设置底部视图
可自定义设置底部视图。
```java
public void setSplashBottomLayoutView(ViewGroup view, boolean autofit);
```
**注**:`autofit`为 `NO`强制添加底部视图，为`YES`时将根据素材大小自动适配底部视图。
**注**:横屏模式下，此设置无效。


## 广告回调
使用以下回调接收加载广告的事件

`APAdSplashListener`

```java
// 当广告成功填充，并加载完成后触发此回调
public void onAPAdSplashLoadSuccess(APAdSplash ad);

// 当广告填充或者加载失败后触发此回调
public void onAPAdSplashLoadFail(APAdSplash ad, APAdError err);

// 当广告渲染成功触发此回调
public void onAPAdSplashRenderSuccess(APAdSplash ad);

// 当广告成功展示后触发此回调
public void onAPAdSplashPresentSuccess(APAdSplash ad);

// 当广告成功展示失败后触发此回调
public void onAPAdSplashPresentFail(APAdSplash ad, APAdError err);

// 当广告被点击后触发此回调
public void onAPAdSplashClick(APAdSplash ad);

// 当广告被点击后完成展示落地页
public void onApAdSplashDidPresentLanding(APAdSplash ad);

// 当广告加载完毕落地页后关闭落地页
public void onApAdSplashDidDismissLanding(APAdSplash ad);

// 当广点关闭落地页后将跳转出应用
public void onApAdSplashApplicationWillEnterBackground(APAdSplash ad)

// 当广告将已经被关闭后触发此回调
public void onAPAdSplashDismiss(APAdSplash ad);

// 广告每展示200毫秒触发一次此回调
// 广告自动关闭前一定触发最后一次并且time = 0;
// @param time 广告展示停留剩余时间，单位：毫秒
public void onAPAdSplashPresentTimeLeft(int time);
```

# <a id="interstitialAD">接入插屏广告</a>
## 创建插屏实例
`APInterstitial interstitial = new APInterstitial("slotID", interstitialListener`

## 设置Deeplink弹窗
设置Deeplink弹窗 - 如果不调用此方法设置，则点击广告不弹窗，直接跳转目标应用。
```java
public void setDeeplinkTipWithTitle(String title);
```
## 加载广告
调用下面方法加载广告
```java
public void load();
```

## 展示广告
调用下面方法展示广告

```java
public void presentWithViewContainer(Activity activity);
```

## 释放资源
在不需要该插屏实例时（每次插屏展示完毕，需要再次展示时需要重新创建插屏实例），需要调用方法：`destroy()`来释放所占用的资源。
```java
public void destroy();
```

## 插屏广告回调
使用以下回调接收加载广告的事件
`APAdInterstitialListener`

```java
// 当广告成功填充，并加载完成后触发此回调
public void onAPAdInterstitialLoadSuccess(APAdInterstitial ad);

// 当广告填充或者加载失败后触发此回调
public void onAPAdInterstitialLoadFail(APAdInterstitial ad, APAdError err);

// 当广告成功展示后触发此回调
public void onAPAdInterstitialPresentSuccess(APAdInterstitial ad);

// 当广告成功展示失败后触发此回调
public void onAPAdInterstitialPresentFail(APAdInterstitial ad, APAdError err);

// 当广告被点击后触发此回调
public void onAPAdInterstitialClick(APAdInterstitial ad);

// 当展示落地页触发此回调
public void onApAdInterstitialDidPresentLanding(APAdInterstitial ad);

// 当加载完毕落地页后关闭落地页
public void onApAdInterstitialDidDismissLanding(APAdInterstitial ad);

// 当将跳转出应用时触发此回调
public void onApAdInterstitialApplicationWillEnterBackground(APAdInterstitial ad)

// 当广告已经关闭后触发此回调
public void onAPAdInterstitialDismiss(APAdInterstitial ad);
```
# <a id="rewardedVideoAd">接入激励视频广告</a>
## 创建激励视频实例
`APAdRewardVideo rewardVideo = new APAdRewardVideo("slotID", APAdRewardVideoListener`

## 设置Deeplink弹窗
设置Deeplink弹窗 - 如果不调用此方法设置，则点击广告不弹窗，直接跳转目标应用。
```java
public void setDeeplinkTipWithTitle(String title);
```
## 加载广告
调用下面方法加载广告
```java
public void load();
```

## 展示广告
调用下面方法展示广告

```java
public void presentWithViewContainer(Activity activity);
```


## 激励视频广告回调
使用以下回调接收加载广告的事件
`APAdRewardVideoListener`

```java
// 当广告成功填充，并加载完成后触发此回调
public void onAPAdRewardVideoLoadSuccess(APAdRewardVideo ad);

// 当广告填充或者加载失败后触发此回调
public void onAPAdRewardVideoLoadFail(APAdRewardVideo ad, APAdError err);

// 广告展示成功回调
public void onAPAdRewardVideoPresentSuccess(APAdRewardVideo ad);

// 广告展示失败回调
public void onAPAdRewardVideoPresentFail(APAdRewardVideo ad, APAdError err);

// 当广告被点击后触发此回调
public void onAPAdRewardVideoClick(APAdRewardVideo ad);

// 当广告已经播放完成 - 激励条件达成
public void onAPAdRewardVideoDidPlayComplete(APAdRewardVideo ad);

// 当广告已经关闭后触发此回调
public void onAPAdRewardVideoDismiss(APAdRewardVideo ad);
```

# <a id="bannerAD">接入横幅广告</a>
## 创建横幅广告实例
`APAdBannerView banner = new APAdBannerView("slotID", adSize, listener);`
## 设置期望尺寸
需要在load前设置bannerview的尺寸
```java
public void setImageAcceptedSize(int width, int height);
```
## 设置banner自动轮播时长
当该值小于20秒时，广告不轮换，超过120秒会默认为120秒。
需要在load前设置刷新时长
```java
public void setRefreshTimer(int refreshTimer);
```

## 设置Deeplink弹窗
设置Deeplink弹窗 - 如果不调用此方法设置，则点击广告不弹窗，直接跳转目标应用。
```java
public void setDeeplinkTipWithTitle(String title);
```

## 加载广告
调用下面方法加载广告
```java
public void load();
```

## 释放资源
在不需要该横幅实例时，需要调用方法：`destroy()`来释放所占用的资源。
```java
public void destroy();
```


## APAdBannerView广告回调
使用以下回调接收加载广告的事件
`APAdBannerListener`

```java
// 当广告成功填充，并加载完成后触发此回调
public void onAPAdBannerViewLoadSuccess(APAdBannerView ad);

// 当广告填充或者加载失败后触发此回调
public void onAPAdBannerViewLoadFail(APAdBannerView ad, APAdError err);

// 广告展示成功回调
public void onAPAdBannerViewPresentSuccess(APAdBannerView ad);

// 当广告被点击后触发此回调
public void onAPAdBannerViewClick(APAdBannerView ad);
```



# <a id="others">其他</a>

* 支持架构：只支持：armeabi-v7a, armeabi, arm64-v8a


# <a id="errorCode">SDK错误码</a>

``` java
    APAdStatusCodeNoFill                        = 51002,    // Ad is not filled
    APAdStatusCodeDuplicateRequest              = 51003,    // Instance of ad is already served, usually caused duplicated request on same instance of ad
    APAdStatusCodeMissingSlotConfig             = 51005,    // Ad slot has not been correctly configured
    APAdStatusCodeAdNotLoaded                   = 51006,    // Ad is not loaded, load ad first
    APAdStatusCodeDuplicatePresent              = 51007,    // Ad present duplicated against the same instance
    APAdStatusCodeSlotNotAvailable              = 51008,    // Ad slot is not available for your request
    APAdStatusCodeScreenOrientationIncompatible = 51009,    // Ad is unable to be presented when orientation changed since request
    APAdStatusCodeSplashContainerUnvisible      = 51011,    // ViewContainer for Splash is not visible
    APSDKStatusCodeSDKNotInitialized            = 59991,    // SDK has not been initialized
    APSDKStatusCodeNoPreliminaryRun             = 59992,    // SDK needs to be initialized successfully at least once
    APSDKStatusCodeConfigUnavailable            = 59993,    // SDK has not been configured, contact adop team
    APSDKStatusCodeInvalidRequestPath           = 59994,    // Network is currently not available
    APSDKStatusCodeNetworkUnavailable           = 59995,    // Request path is invalid
    APSDKStatusCodeNetworkTimeout               = 59996,    // Request timeout, please try again later
    APSDKStatusCodeInternalError                = 59997,    // There has been an internal error
    APSDKStatusCodeServerError                  = 59998,    // Request has returned an error
    APSDKStatusCodeUnknown                      = 59999    // Some thing went terribly wrong!
```


# <a id="English">AppicAd SDK Integration guide</a>

* [Preparation](#essential_en)
* [Native](#nativeAD_en)
* [Splash](#splashAD_en)
* [Interstitial Ad](#interstitialAD_en)
* [BannerView Ad](#bannerAD_en)
* [Rewarded Video](#videoAD_en)
* [More](#others_en)


## <a id="essential_en">Preparation</a>
* Add the following dependencies to your project `build.gradle`:
```java
implementation 'com.android.support:support-v4:28.0.0'
implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
```


* To initialize SDK, please add the following line to onCreate delegate of your main activity or Application(recommended).

	```
	APSDK.init(context, "appID",listener);
	```
	Please initialize the SDK on main process, and make sure have received its success recall before sending any ad requests.
        **Note**:  `appID` is provided by our operator or you can find them from the App page of the dashboard.


* `proguard` configuration:

```
	-keep public class com.google.android.gms.**
	-dontwarn com.google.android.gms.**
	-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
     public *;
	 }
	 -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
     public *;
	 }
	 
	-keep class com.adup.** {*;}
	-keep class com.apd.** { *; }
	-dontwarn com.adup.**
```
* Resource Obfuscation
    Add following content into a `keep.xml` file under your `res/raw/` folder
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <resources xmlns:tools="http://schemas.android.com/tools"
    tools:keep="@layout/ap_*,@anim/ap_*,@drawable/ap_*,@string/ap_*,@style/ap_*,@dimen/ap_*,@color/ap_*,@bool/ap_*,@raw/apsdk_*,@xml/ap_*"
    />
    ```

   If you use AabResGuard plugin to handle resource obfuscation of your project, please add this content into whitelist:
    ```
    "*.R.id.ap_*",
    "*.R.anim.ap_*",
    "*.R.drawable.ap_*",
    "*.R.layout.ap_*",
    "*.R.string.ap_*",
    "*.R.dimen.ap_*",
    "*.R.color.ap_*",
    "*.R.bool.ap_*",
    "*.R.style.ap_*",
    "*/res/anim/ap_*",
    "*/res/drawable/ap_*",
    "*/res/drawable-xxhdpi-v4/ap_*",
    "*/res/layout/ap_*",
    "*/res/raw/apsdk_*",
    "*/res/xml/ap_*" 
    ```

**Note**:The device imei can be passed in to receive better monetization effect

#### get SDK Version
```java
public String sdkVersion();
```

#### APSDKListener
```java
// The SDK initialized successfully
public void onSDKInitializeSuccess();

// The SDK failed to initialize
public void onSDKInitializeFail(APAdError err);
```


## <a id="nativeAD_en">Native</a>
### Instantiation
```java
APAdNative apNative = new APAdNative("placement ID", listener);
```

| Parameters	|	Description |
| ---	|	--- |
| activity	|	Current activity when creating the instance |
| placement ID	|	placement ID |
| listener	|	Listener to received call backs |

### load

```java
apNative.load();
```

**Once ad is loaded succesfully by receiving load success call back, you can start using ad assets with the following getter methods.**

| Description	 | 	Getter                   | Note |
|--------------|---------------------------| --- |
| icon	        | 	`getAPAdIcon()`          | |
| icon url	    | 	`getAPAdIcon()`          | |
| image	       | 	`getAPAdScreenshot()`    | |
| image url	   | 	`getAPAdScreenshotUrl()` | |
| description	 | 	`getAPAdDescription()`   | |
| title	       | 	`getAPAdTitle()`         ||
| video	       | 	`getAPAdVideo()`         |if ad type is not `video`, The return value is null|



**Set Binder**

set a `binder` to tell sdk what view components the native ad's content were used on. Need to be set before call `BindAdToView`.

```
public void setViewBinder(APNativeViewBinder binder)
```

example of how to create a `binder` instance：
```
APNativeViewBinder.Builder.create().setTitleViewID(titleViewID).setDescViewID(descViewID).setIconViewID(iconViewID).setScreenshotViewID(screenshotViewID).build();
```


**BindAdToView**

Binding of the View and the ad must be set for APAdNativeAdContainer.

clickableViews is the View that triggers the ad click behavior, it must be in the container, otherwise it will not respond to the click event.

Do not set OnClickListener on clickableViews, it will affect the click event report, click event callback can be received through onApAdNativeDidClick callback

```java
public void bindAdToView(APAdNativeAdContainer container, List<View> clickableViews)
```
**API-Status Management

Call this when the ad located Activity.onResume(), to reset the ad status, otherwise the status may be incorrect. This is necessary only when the app is also published in China mainland.
```java
public void onResume();
```
**Set deeplink prompt**
If this method is not called, the deeplink alert is default to set to Off
```java
public void setDeeplinkTipWithTitle(String title);
```

### Listener
`APAdNativeListner`

```java
// when Native ad loaded successfully.
// @param ad: the ad for Native
public void onApAdNativeDidLoadSuccess(APAdNative ad);

// when Native ad slot failed to load.
// @param ad: the ad for Native
// @param err the reason of error
public void onApAdNativeDidLoadFail(APAdNative ad, APAdError err);

// when a Native ad presents successully.
// @param ad the ad for Native
public void onAPAdNativePresentSuccess(APAdNative ad);

// when a Native ad is clicked.
// @param ad the ad for Native
public void onApAdNativeDidClick(APAdNative ad);

// when the landing page presents.
// @param ad: the ad for Native
public void onApAdNativeDidPresentLanding(APAdNative ad);

// when the landing page is dismissed.
// @param ad: the ad for Native
public void onApAdNativeDidDismissLanding(APAdNative ad);

// when the ad will enter background.
// @param ad: the ad for Native
public void onApAdNativeApplicationWillEnterBackground(APAdNative ad);
```
**`APAdNativeVideoView`**
```java
// When the video finishes
// @param view NativeVideo View
public void onApAdNativeVideoViewDidPlayFinish(APAdNativeVideoView view);
```

## APAdNativeVideoView - methods

| Method	|	Feature | Note |
| ---	|	--- | --- |
| `public void setMute(boolean mute);`	|	Mute when playing |mute: `YES`mute on，`NO`：mute off |
| `public void play() ;`	|	Play video | |
| `public void pause();`	|	Pause video | |


**`APAdNativeVideoState`**
| State	|	Description |
| ---	|	--- |
| `APAdNativeVideoStateDefault`	|	video default state |
| `APAdNativeVideoStateFailed`	| video play failed |
| `APAdNativeVideoStateBuffering`	|	video buffering |
| `APAdNativeVideoStatePlaying`	|	video playing |
| `APAdNativeVideoStateStop`	|	video playback stopped |
| `APAdNativeVideoStatePause`	|	video playback paused |



## <a id="splashAD_en">APAdSplash</a>
### Instantiation

```java
APAdSplash splash = new APAdSplash(Placement ID,listener);
```

| Parameters	|	Description |
| --- | --- |
| Placement ID	|	Placement ID |
| listener	|	Listener to received call backs |

### Load Splash ad

```java
public void load();
```

#### Show Splash ad

```java
public void presentWithViewContainer(ViewGroup container);
```
* **ViewGroup** - ViewGroup for which add ad will be displayed within

#### Load & Show
Call this method to load and show the ad immediately`APAdSplash`

```java
public void loadAndPresentWithViewContainer(ViewGroup container);
```
* **ViewGroup** - ViewGroup for which ad will be displayed within
* **Note**:`loadAndPresent` and `load`cannot be used at the same time.

#### Set load duration
duration >=0 ，default is 3s.

```java
public boolean setSplashMaxLoadInterval(double interval); //Unit: second
```

#### Set display duration

```java
public boolean setSplashShowInterval(int interval); //Unit: second
```

#### Set backgroundColor
Call this to set a color for background, default:black.
```java
public void setSplashBackgroundColor(int color);
```

#### Set backgroung image
default: None
```java
public void setSplashBackgroundColor(Bitmap image);
```
**Note** :The background image will cover the background color

#### Customize the close button style
```java
public void setSplashCloseButtonView(View view);
```

#### Customize the close button position
```java
public void setSplashCloseButtonPosition(ViewGroup.LayoutParams params);
```

#### Set deeplink prompt
If this method is not called, the deeplink alert will not show
```java
public void setDeeplinkTipWithTitle(String title);
```

#### Set bottomView
```java
public void setSplashBottomLayoutView(ViewGroup view, boolean autofit);
```
**Note**:`NO`:Force display bottomView,`YES`:Automatically adapt according to the size of the material.
**Note**:BottomView is invalid in landscape mode

### APAdSplashListener


```java
// when Splash ad slot loaded successfully.
// @param ad: ad for Splash
public void onAPAdSplashLoadSuccess(APAdSplash ad);

// when Splash ad slot failed to load.
// @param ad: the ad for Splash
// @param err the reason of error
public void onAPAdSplashLoadFail(APAdSplash ad, APAdError err);

// when Splash ad slot render seccess.
public void onAPAdSplashRenderSuccess(APAdSplash ad);

// When splash ad slot presented successfully.
// @param ad : the ad for splash
public void onAPAdSplashPresentSuccess(APAdSplash ad);

// When splash ad slot failed to present.
// @param ad : the ad for splash
// @param err : the reason of error
public void onAPAdSplashPresentFail(APAdSplash ad, APAdError err);

// When splash is clicked.
// @param ad : the ad for splash
public void onAPAdSplashClick(APAdSplash ad);

// When splash landing is presented.
// @param ad : the ad for splash
public void onApAdSplashDidPresentLanding(APAdSplash ad);

// When splash landing is dismissed.
// @param ad : the ad for splash
public void onApAdSplashDidDismissLanding(APAdSplash ad);

// When application will enter background.
// @param ad : the ad for splash
public void onApAdSplashApplicationWillEnterBackground(APAdSplash ad)

// When splash is dismissed.
// @param ad the ad for splash
public void onAPAdSplashDismiss(APAdSplash ad);

// The present time left.
// This will callback for each 200 ms during presentation.
// @param time : the left time for splash to present,  unit: ms.
public void onAPAdSplashPresentTimeLeft(int time);
```

# <a id="interstitialAD_en">Interstitial</a>
## Create a Interstitial Ad
`APInterstitial interstitial = new APInterstitial("placement Id", interstitialListener`

## Set deeplink prompt
If this method is not called, the deeplink alert will not show
```java
public void setDeeplinkTipWithTitle(String title);
```
## Load `Interstitial` ad
```java
public void load();
```

## Show `Interstitial` Ad

```java
public void presentWithViewContainer(Activity activity);
```

## Destroy 
Once an Interstitial ad is no longer needed, use this method to recycle its assets immediately.
`destroy();`
```java
public void destroy();
```

## APAdInterstitialListener

```java
// Interstitial ad slot loaded successfully
public void onAPAdInterstitialLoadSuccess(APAdInterstitial ad);

// Interstitial ad slot failed to load.
public void onAPAdInterstitialLoadFail(APAdInterstitial ad, APAdError err);

// Interstitial ad slot presented successfully.
public void onAPAdInterstitialPresentSuccess(APAdInterstitial ad);

// Interstitial ad slot failed to present.
public void onAPAdInterstitialPresentFail(APAdInterstitial ad, APAdError err);

// Interstitial ad is clicked.
public void onAPAdInterstitialClick(APAdInterstitial ad);

// Interstitial ad landing is presented.
public void onApAdInterstitialDidPresentLanding(APAdInterstitial ad);

// Interstitial ad landing is dismissed.
public void onApAdInterstitialDidDismissLanding(APAdInterstitial ad);

// After ad is clicked, Interstitial ad application will enter background.
public void onApAdInterstitialApplicationWillEnterBackground(APAdInterstitial ad)

// Interstitial ad is dismissed.
public void onAPAdInterstitialDismiss(APAdInterstitial ad);
```
# <a id="rewardedVideoAd_en">RewardVideo</a>
## Create a `APAdRewardVideo` Ad
`APAdRewardVideo rewardVideo = new APAdRewardVideo("Placement Id", APAdRewardVideoListener`

## Set deeplink prompt
If this method is not called, the deeplink alert will not show
```java
public void setDeeplinkTipWithTitle(String title);
```
## Load `APAdRewardVideo` ad
```java
public void load();
```

## Present `APAdRewardVideo` Ad

```java
public void presentWithViewContainer(Activity activity);
```


## APAdRewardVideoListener

```java
// RewardVideo ad slot loaded successfully
public void onAPAdRewardVideoLoadSuccess(APAdRewardVideo ad);

// RewardVideo ad slot failed to load.
public void onAPAdRewardVideoLoadFail(APAdRewardVideo ad, APAdError err);

// RewardVideo ad slot presented successfully.
public void onAPAdRewardVideoPresentSuccess(APAdRewardVideo ad);

// RewardVideo ad slot failed to present.
public void onAPAdRewardVideoPresentFail(APAdRewardVideo ad, APAdError err);

// RewardVideo ad is clicked.
public void onAPAdRewardVideoClick(APAdRewardVideo ad);

// RewardVideo ad is played completely.
public void onAPAdRewardVideoDidPlayComplete(APAdRewardVideo ad);

// RewardVideo ad is dismissed.
public void onAPAdRewardVideoDismiss(APAdRewardVideo ad);
```

# <a id="bannerAD_en">BannerView Ad</a>
## Create a `APAdBannerView` Ad
`APAdBannerView banner = new APAdBannerView("Placement Id", adSize, listener);`
## Set the expected size
you should set it before load bannerview
```java
public void setImageAcceptedSize(int width, int height);
```
## Set the banner refresh time
The setting does not take effect if the value is less than 20 seconds and defaults to 120 if the value is greater than 120 seconds
you should set it before load bannerview
```java
public void setRefreshTimer(int refreshTimer);
```

## Set deeplink prompt
If this method is not called, the deeplink alert will not show
```java
public void setDeeplinkTipWithTitle(String title);
```

## Load `APAdBannerView` ad
```java
public void load();
```
## Destroy 
Once an banner ad is no longer needed, use this method to recycle its assets immediately.
```java
public void destroy();
```

## APAdBannerViewListener

```java
// APAdBannerView ad slot loaded successfully
public void onAPAdBannerViewLoadSuccess(APAdBannerView ad);

// APAdBannerView ad slot failed to load.
public void onAPAdBannerViewLoadFail(APAdBannerView ad, APAdError err);

// APAdBannerView ad slot presented successfully.
public void onAPAdBannerViewPresentSuccess(APAdBannerView ad);

// APAdBannerView ad is clicked.
public void onAPAdBannerViewClick(APAdBannerView ad);
```

## <a id="others_en">etc</a>

* supported cpu architecture: `armeabi-v7a`、`armeabi`、`arm64-v8a`

# <a id="errorCode">SDK Error Code</a>

``` java
    APAdStatusCodeNoFill                        = 51002,    // Ad is not filled
    APAdStatusCodeDuplicateRequest              = 51003,    // Instance of ad is already served, usually caused duplicated request on same instance of ad
    APAdStatusCodeMissingSlotConfig             = 51005,    // Ad slot has not been correctly configured
    APAdStatusCodeAdNotLoaded                   = 51006,    // Ad is not loaded, load ad first
    APAdStatusCodeDuplicatePresent              = 51007,    // Ad present duplicated against the same instance
    APAdStatusCodeSlotNotAvailable              = 51008,    // Ad slot is not available for your request
    APAdStatusCodeScreenOrientationIncompatible = 51009,    // Ad is unable to be presented when orientation changed since request
    APAdStatusCodeSplashContainerUnvisible      = 51011,    // ViewContainer for Splash is not visible
    APSDKStatusCodeSDKNotInitialized            = 59991,    // SDK has not been initialized
    APSDKStatusCodeNoPreliminaryRun             = 59992,    // SDK needs to be initialized successfully at least once
    APSDKStatusCodeConfigUnavailable            = 59993,    // SDK has not been configured, contact adop team
    APSDKStatusCodeInvalidRequestPath           = 59994,    // Network is currently not available
    APSDKStatusCodeNetworkUnavailable           = 59995,    // Request path is invalid
    APSDKStatusCodeNetworkTimeout               = 59996,    // Request timeout, please try again later
    APSDKStatusCodeInternalError                = 59997,    // There has been an internal error
    APSDKStatusCodeServerError                  = 59998,    // Request has returned an error
    APSDKStatusCodeUnknown                      = 59999    // Some thing went terribly wrong!
```

