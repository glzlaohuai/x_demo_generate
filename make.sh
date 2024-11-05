#!/bin/bash

# 检查是否传入了四个参数
if [ "$#" -ne 4 ]; then
    echo "Usage: $0 <URL> <AppID> <APK Package Name> <SDK Package Name>"
    exit 1
fi

# 读取参数
url=$1
appid=$2
apk_package_name=$3
sdk_package_name=$4

# 下载ZIP文件
zip_file=$(basename "$url")
echo "Downloading $zip_file from $url..."
wget "$url" -O "$zip_file"

# 检查下载是否成功
if [ ! -f "$zip_file" ]; then
    echo "Download failed."
    exit 1
fi

# 解压ZIP文件
echo "Unzipping $zip_file..."
unzip -o "$zip_file"

# 找到AAR文件
aar_file=$(find . -name "*.aar" | head -n 1)
if [ -z "$aar_file" ]; then
    echo "AAR file not found in the unzipped directory."
    exit 1
fi

# 移动AAR文件到app/libs目录下
echo "Moving AAR file to app/libs..."
mkdir -p app/libs
mv "$aar_file" app/libs/

# 替换app/build.gradle中的包名
echo "Updating app/build.gradle with new package name..."
sed -i.bak "s/com.apicmob.sdk.demo/$apk_package_name/g" app/build.gradle

# 替换MainActivity.java中的AppID
echo "Updating MainActivity.java with new AppID..."
sed -i.bak "s/__appid__/$appid/g" app/src/main/java/com/appicplay/helloworld/MainActivity.java

# 替换MainActivity.java中的SDK包名
echo "Updating MainActivity.java with new SDK package name..."
sed -i.bak "s/com.ap.android.trunk/$sdk_package_name/g" app/src/main/java/com/appicplay/helloworld/MainActivity.java
# 删除备份文件
find . -name "*\.bak" -type f -delete

# 执行gradlew assembleRelease
echo "Building the project..."
./gradlew assembleRelease

# 清理下载的ZIP文件
echo "Cleaning up..."
rm "$zip_file"

echo "Process completed."