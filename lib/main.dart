import 'package:flutter/material.dart';
import 'package:home_widget_listview_example/service/home_widget_service.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String homeWidgetText = '';

  @override
  void initState() {
    HomeWidgetService().getAppLaunchedArticleId().then((data) {
      homeWidgetText = data;
      setState(() {});
    });

    HomeWidgetService().listenAppLaunchedArticleId.listen((data) {
      homeWidgetText = data;
      setState(() {});
    });
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: Scaffold(
          body: Center(
        child: Text(homeWidgetText),
      )),
    );
  }
}
