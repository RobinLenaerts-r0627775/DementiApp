using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Essentials;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DementiApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class MemoryMediumPage : ContentPage
    {

        private readonly HttpClient _client = new HttpClient();
        private String userid;
        private Button clicked;
        int score = 0;
        Dictionary<string, Color> layout = new Dictionary<string, Color>();
        Dictionary<string, string> pics = new Dictionary<string, string>();
        List<Post> patientpics = new List<Post>();

        /**
         * Constructor method for the page. 
         * Sets userid and loads page from xaml. 
         */
        public MemoryMediumPage(String userId)
        {
            userid = userId;
            InitializeComponent();
            for (int i = 0; i < 20; i++)
            {
                (ButtonGrid.FindByName("f" + i) as Button).IsEnabled = false;
                (ButtonGrid.FindByName("f" + i) as Button).Margin = 10;

            }
        }

        /**
         * little helper method for displayalerts.
         */
        public async void showMessage()
        {
            await DisplayAlert("Info", "Klik op een vakje om het om de foto erachter te zien. Probeer alle paren te vinden.", "Oké!");
        }

        /**
         * Method that works async to get information from the server through the API.
         * Then loads the needed information onto the page. 
         */

        protected async override void OnAppearing()
        {
            showMessage();
            try
            {
                string content = await _client.GetStringAsync("http://193.191.177.178:8080/api/media/" + userid);
                List<Post> photos = JsonConvert.DeserializeObject<List<Post>>(content);
                foreach (Post p in photos)
                {
                    Byte[] byteArray = await _client.GetByteArrayAsync("http://193.191.177.178:8080/api/media/data/" + p.MediaId);

                    p.Data = ImageSource.FromStream(() => new MemoryStream(byteArray));
                }
                patientpics = photos;
                patientpics.Shuffle();
            }
            catch (Exception e)
            {
                await DisplayAlert("Error", e.Message, "Error while loading images.");
            }
            var numbers = new List<int>(Enumerable.Range(0, 20));
            numbers.Shuffle();
            Device.StartTimer(TimeSpan.FromSeconds(2), () =>
            {
                foreach (int i in numbers)
                {
                    //but.Image = pic;
                    Image image = ButtonGrid.FindByName("f0" + i) as Image;
                    if (image.Source == null && patientpics.Count() > 9)
                    {
                        // So it doesn't eat up clicks that should go to the button:
                        image.InputTransparent = true;
                        // Give it a margin so it doesn't extend to the edge of the grid
                        image.Margin = new Thickness(10);
                        image.IsVisible = false;
                        ButtonGrid.Children.Add(image);

                        int x = 0;
                        int y = 0;
                        int z = i;
                        x = z / 4;
                        y = z % 4;
                        Grid.SetRow(image, y);
                        Grid.SetColumn(image, x + 1);
                    }

                    if (patientpics.Count() > 9)
                    {
                        switch (numbers.IndexOf(i))
                        {
                            case 0:
                                layout.Add("" + i, Color.Magenta);
                                image.Source = patientpics.ElementAt(0).Data;
                                break;
                            case 1:
                                layout.Add("" + i, Color.Magenta);
                                image.Source = patientpics.ElementAt(0).Data;
                                break;
                            case 2:
                                layout.Add("" + i, Color.LimeGreen);
                                image.Source = patientpics.ElementAt(1).Data;
                                break;
                            case 3:
                                layout.Add("" + i, Color.LimeGreen);
                                image.Source = patientpics.ElementAt(1).Data;
                                break;
                            case 4:
                                layout.Add("" + i, Color.Yellow);
                                image.Source = patientpics.ElementAt(2).Data;
                                break;
                            case 5:
                                layout.Add("" + i, Color.Yellow);
                                image.Source = patientpics.ElementAt(2).Data;
                                break;
                            case 6:
                                layout.Add("" + i, Color.Green);
                                image.Source = patientpics.ElementAt(3).Data;
                                break;
                            case 7:
                                layout.Add("" + i, Color.Green);
                                image.Source = patientpics.ElementAt(3).Data;
                                break;
                            case 8:
                                layout.Add("" + i, Color.OrangeRed);
                                image.Source = patientpics.ElementAt(4).Data;
                                break;
                            case 9:
                                layout.Add("" + i, Color.OrangeRed);
                                image.Source = patientpics.ElementAt(4).Data;
                                break;
                            case 10:
                                layout.Add("" + i, Color.Brown);
                                image.Source = patientpics.ElementAt(5).Data;
                                break;
                            case 11:
                                layout.Add("" + i, Color.Brown);
                                image.Source = patientpics.ElementAt(5).Data;
                                break;
                            case 12:
                                layout.Add("" + i, Color.Gray);
                                image.Source = patientpics.ElementAt(6).Data;
                                break;
                            case 13:
                                layout.Add("" + i, Color.Gray);
                                image.Source = patientpics.ElementAt(6).Data;
                                break;
                            case 14:
                                layout.Add("" + i, Color.MidnightBlue);
                                image.Source = patientpics.ElementAt(7).Data;
                                break;
                            case 15:
                                layout.Add("" + i, Color.MidnightBlue);
                                image.Source = patientpics.ElementAt(7).Data;
                                break;
                            case 16:
                                layout.Add("" + i, Color.Moccasin);
                                image.Source = patientpics.ElementAt(8).Data;
                                break;
                            case 17:
                                layout.Add("" + i, Color.Moccasin);
                                image.Source = patientpics.ElementAt(8).Data;
                                break;
                            case 18:
                                layout.Add("" + i, Color.YellowGreen);
                                image.Source = patientpics.ElementAt(9).Data;
                                break;
                            case 19:
                                layout.Add("" + i, Color.YellowGreen);
                                image.Source = patientpics.ElementAt(9).Data;
                                break;
                            default:
                                layout.Add("" + i, Color.Black);
                                image.Source = patientpics.ElementAt(0).Data;
                                break;
                        }
                    }
                    else
                    {
                        switch (numbers.IndexOf(i))
                        {
                            case 0:
                                layout.Add("" + i, Color.Magenta);
                                pics.Add("" + i, "Luci.jpg");
                                break;
                            case 1:
                                layout.Add("" + i, Color.Magenta);
                                pics.Add("" + i, "Luci.jpg");
                                break;
                            case 2:
                                layout.Add("" + i, Color.LimeGreen);
                                pics.Add("" + i, "Decker.jpg");
                                break;
                            case 3:
                                layout.Add("" + i, Color.LimeGreen);
                                pics.Add("" + i, "Decker.jpg");
                                break;
                            case 4:
                                layout.Add("" + i, Color.Yellow);
                                pics.Add("" + i, "Maze.jpg");
                                break;
                            case 5:
                                layout.Add("" + i, Color.Yellow);
                                pics.Add("" + i, "Maze.jpg");
                                break;
                            case 6:
                                layout.Add("" + i, Color.Green);
                                pics.Add("" + i, "Linda.jpg");
                                break;
                            case 7:
                                layout.Add("" + i, Color.Green);
                                pics.Add("" + i, "Linda.jpg");
                                break;
                            case 8:
                                layout.Add("" + i, Color.OrangeRed);
                                pics.Add("" + i, "Dan.jpg");
                                break;
                            case 9:
                                layout.Add("" + i, Color.OrangeRed);
                                pics.Add("" + i, "Dan.jpg");
                                break;
                            case 10:
                                layout.Add("" + i, Color.Brown);
                                pics.Add("" + i, "Amenadiel.jpg");
                                break;
                            case 11:
                                layout.Add("" + i, Color.Brown);
                                pics.Add("" + i, "Amenadiel.jpg");
                                break;
                            case 12:
                                layout.Add("" + i, Color.Gray);
                                pics.Add("" + i, "Ella.jpg");
                                break;
                            case 13:
                                layout.Add("" + i, Color.Gray);
                                pics.Add("" + i, "Ella.jpg");
                                break;
                            case 14:
                                layout.Add("" + i, Color.MidnightBlue);
                                pics.Add("" + i, "Nolan.jpg");
                                break;
                            case 15:
                                layout.Add("" + i, Color.MidnightBlue);
                                pics.Add("" + i, "Nolan.jpg");
                                break;
                            case 16:
                                layout.Add("" + i, Color.Moccasin);
                                pics.Add("" + i, "Talia.jpg");
                                break;
                            case 17:
                                layout.Add("" + i, Color.Moccasin);
                                pics.Add("" + i, "Talia.jpg");
                                break;
                            case 18:
                                layout.Add("" + i, Color.YellowGreen);
                                pics.Add("" + i, "Trixie.jpg");
                                break;
                            case 19:
                                layout.Add("" + i, Color.YellowGreen);
                                pics.Add("" + i, "Trixie.jpg");
                                break;
                            default:
                                layout.Add("" + i, Color.Black);
                                pics.Add("" + i, "Luci.jpg");
                                break;
                        }
                    }
                    (ButtonGrid.FindByName("f" + i) as Button).IsEnabled = true;
                }
                return false;
            });
        }

        /**
         * method that handles the clicks on the memorygame. shows the images and hides them if there is no match. 
         * also checks if there are enough personal fotos in the patient's photo repository to play the game. if not: use default photos.
         * makes the device vibrate whenever a valid pair is found.
         * 
         */
        private void Button_Clicked(object sender, EventArgs e)
        {
            Button but = (Button)sender;
            if (patientpics.Count() > 9)
            {
                but.IsVisible = false;
            Image fullscreen = new Image();
            fullscreen.GestureRecognizers.Add(new TapGestureRecognizer { Command = new Command(() => { ButtonGrid.Children.Remove(fullscreen); }), NumberOfTapsRequired = 1 });

            fullscreen.Source = (but.Parent.FindByName("f0" + but.StyleId) as Image).Source;
            ButtonGrid.Children.Add(fullscreen);
            Grid.SetColumn(fullscreen, 1);
            Grid.SetColumnSpan(fullscreen, 5);
            Grid.SetRow(fullscreen, 0);
            Grid.SetRowSpan(fullscreen, 4);
            }
            but.IsEnabled = false;
            but.BorderColor = Color.LightYellow;
            Color col = Color.Red;
            layout.TryGetValue(but.StyleId, out col);
            but.BackgroundColor = col;
            pics.TryGetValue(but.StyleId, out string pic);
            if (patientpics.Count > 9)
            {
                Image image = but.Parent.FindByName("f0" + but.StyleId) as Image;
                image.IsVisible = true;
            }
            else
            {
                but.Image = pic;
            }
            if (clicked == null) clicked = but;
            else
            {
                if (col.Equals(clicked.BackgroundColor))
                {
                    try
                    {
                        Vibration.Vibrate();
                    }
                    catch (FeatureNotSupportedException ex)
                    {
                        Console.WriteLine(ex.StackTrace);
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine(ex.StackTrace);
                        //ex.StackTrace;
                        //throw;
                    }
                    but.IsEnabled = false;
                    clicked.IsEnabled = false;
                    but.BorderColor = Color.Transparent;
                    but.BackgroundColor = Color.Transparent;
                    clicked.BackgroundColor = Color.Transparent;
                    clicked.BorderColor = Color.Transparent;
                    clicked = null;
                    score += 1;
                    if (score == 10)
                    {
                        Navigation.PushAsync(new WinPage(userid));
                    }

                }
                else
                {
                    for (int i = 0; i < 20; i++)
                    {
                        (but.Parent.FindByName("f" + i) as Button).IsEnabled = false;
                    }

                    Device.StartTimer(TimeSpan.FromSeconds(2), () =>
                    {
                        if (patientpics.Count > 9)
                        {
                            (but.Parent.FindByName("f0" + but.StyleId) as Image).IsVisible = false;
                            (but.Parent.FindByName("f0" + clicked.StyleId) as Image).IsVisible = false;
                        }
                        else
                        {
                            clicked.Image = null;
                            but.Image = null;
                        }
                        but.IsEnabled = true;
                        clicked.IsEnabled = true;
                        but.IsVisible = true;
                        clicked.IsVisible = true;
                        but.BorderColor = Color.Transparent;
                        clicked.BorderColor = Color.Transparent;
                        clicked.BackgroundColor = Color.Red;
                        but.BackgroundColor = Color.Red;
                        clicked = null;
                        for (int i = 0; i < 20; i++)
                        {
                            if ((but.Parent.FindByName("f" + i) as Button).BackgroundColor == Color.Red) (but.Parent.FindByName("f" + i) as Button).IsEnabled = true;
                        }
                        return false;
                    });

                }
            }
        }
    }

   
}