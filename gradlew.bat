import 'dart:ui';
import 'package:coupfferapp/screen/categories_screen.dart';
import 'package:coupfferapp/screen/first_screen.dart';
import 'package:coupfferapp/screen/home_page.dart';
import 'package:coupfferapp/style/color_assets.dart';
import 'package:flutter/material.dart';

class Home extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _HomeState();
  }
}

class _HomeState extends State<Home> {
  int _currentIndex = 1;
  List<Widget> _pages = [FirstScreen(), HomePage(), FirstScreen()];

  @override
  Widget build(BuildContext context) {
    var selectedLabelStyle = TextStyle(color: Colors.white);
    var drawerTextStyle = TextStyle(color: Colors.black38);
    return Scaffold(
      appBar: AppBar(title: Text('Flutter App')),
      body: Stack(
        children: <Widget>[
          _pages[_currentIndex],
          Positioned(
            left: 0,
            right: 0,
            bottom: 0,
            child: ClipRRect(
              borderRadius: BorderRadius.only(
                topRight: Radius.circular(15),
                topLeft: Radius.circular(15),
              ),
              child: BottomNavigationBar(
                elevation: 10,
                backgroundColor: ColorAssets.theamColorBlue,
                currentIndex: _currentIndex,
                onTap: onTabTapped,
                showSelectedLabels: true,
                showUnselectedLabels: false,
                items: [
                  BottomNavigationBarItem(
                    icon: Icon(
                      Icons.notifications_none,
                      color: Colors.white,
                    ),
                    title: Text(
                      'Notification',
                      style: selectedLabelStyle,
                    ),
                  ),
                  BottomNavigationBarItem(
                    icon: Icon(
                      Icons.home,
                      color: Colors.white,
                    ),
                    title: Text(
                      'Home',
                      style: selectedLabelStyle,
                    ),
                  ),
                  BottomNavigationBarItem(
          