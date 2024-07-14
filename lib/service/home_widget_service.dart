import 'dart:developer';

import 'package:home_widget/home_widget.dart';

class HomeWidgetService {
  static const _logName = 'HomeWidgetService';

  /// returns the article id of the home widget news article which launched the app from killed state
  Future<String> getAppLaunchedArticleId() async {
    try {
      final uri = await HomeWidget.initiallyLaunchedFromHomeWidget();
      log(uri.toString(), name: _logName);
      return uri.toString();
    } catch (e) {
      log('Error $e', name: _logName);
      return '';
    }
  }

  /// listens the article id of the home widget news article which launched the app, when app is in background
  Stream<String> get listenAppLaunchedArticleId =>
      HomeWidget.widgetClicked.map((uri) => uri.toString());
}
